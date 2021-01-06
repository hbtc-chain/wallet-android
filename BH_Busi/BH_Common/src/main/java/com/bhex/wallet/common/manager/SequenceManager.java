package com.bhex.wallet.common.manager;

import android.text.TextUtils;
import android.util.ArrayMap;

import com.bhex.network.RxSchedulersHelper;
import com.bhex.network.observer.BHBaseObserver;
import com.bhex.network.observer.SimpleObserver;
import com.bhex.network.utils.JsonUtils;
import com.bhex.tools.utils.LogUtils;
import com.bhex.tools.utils.ToolUtils;
import com.bhex.wallet.common.api.BHttpApi;
import com.bhex.wallet.common.api.BHttpApiInterface;
import com.bhex.wallet.common.base.BaseActivity;
import com.bhex.wallet.common.tx.TransactionOrder;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Sequence 管理
 */
public class SequenceManager {

    private static SequenceManager _instance = new SequenceManager();

    private AtomicInteger sequence = new AtomicInteger(0);

    private ArrayMap<String,PeddingTx> mPeddingTxMap;

    private CompositeDisposable compositeDisposable;


    private SequenceManager(){
        mPeddingTxMap = new ArrayMap<>();
        compositeDisposable = new CompositeDisposable();
    }

    public static SequenceManager getInstance(){
        return _instance;
    }

    public synchronized void initSequence(String v_sequence){
        //sequence = new AtomicInteger(Integer.valueOf(v_sequence));
        if(sequence.get()==0){
            sequence = new AtomicInteger(Integer.valueOf(v_sequence));
        }
    }

    public synchronized String increaseSequence(){
        return String.valueOf(sequence.incrementAndGet());
    }

    public synchronized String getSequence(String v_sequence){
        LogUtils.d("TransactionViewModel===>","peddingTxMap=="+mPeddingTxMap.size());
        /*if(!ToolUtils.checkMapEmpty(mPeddingTxMap)){
            return increaseSequence();
        }else{
            return v_sequence;
        }*/
        if(sequence.get()==0){
            return v_sequence;
        }else{
            return String.valueOf(sequence.get());
        }
    }

    //添加
    public synchronized void putPeddingTranscation(JsonObject jsonObject) {
        //
        TranscationResponse transcationResponse = JsonUtils.fromJson(jsonObject.toString(),TranscationResponse.class);
        if(!ToolUtils.checkListIsEmpty(transcationResponse.logs) && transcationResponse.logs.get(0).success){
            PeddingTx peddingTx = new PeddingTx();
            peddingTx.tx = transcationResponse.txhash;
            peddingTx.time = System.currentTimeMillis();
            mPeddingTxMap.put(transcationResponse.txhash,peddingTx);
            //sequence = new AtomicInteger(Integer.valueOf(increaseSequence()));
            increaseSequence();
            LogUtils.d("TransactionViewModel===>","==sequence==after=="+sequence.get());
        }

    }

    //
    public ArrayMap<String, PeddingTx> getPeddingTxMap() {
        return mPeddingTxMap;
    }



    //更新未打包交易状态
    public void timerTranscation(BaseActivity activity){
        Observable.interval(2000,1000L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleObserver<Long>(){

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        if(compositeDisposable==null){
                            compositeDisposable = new CompositeDisposable();
                        }
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        super.onNext(aLong);
                        queryTransactionDetailExt(activity);
                    }
                });
    }

    public void stopTranscation(BaseActivity activity){
        if(compositeDisposable!=null && !compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    public void queryTransactionDetailExt(BaseActivity activity){
        //LogUtils.d("TransactionViewModel===>","queryTransactionDetailExt=="+mPeddingTxMap.size());
        if(ToolUtils.checkMapEmpty(SequenceManager.getInstance().getPeddingTxMap())){
            return;
        }

        //如果5秒自动移除
        long now = System.currentTimeMillis();
        Iterator<String> itor = mPeddingTxMap.keySet().iterator();
        while (itor.hasNext()) {
            String key = itor.next();
            PeddingTx peddingTx = mPeddingTxMap.get(key);
            if(peddingTx.time+5000<now){
                mPeddingTxMap.remove(key);
            }
        }

        if(ToolUtils.checkMapEmpty(SequenceManager.getInstance().getPeddingTxMap())){
            return;
        }

        String txhash = SequenceManager.getInstance().getPeddingTxMap().keyAt(0);
        queryTransactionDetail(activity,txhash);
    }

    //请求交易
    public void queryTransactionDetail(BaseActivity activity, String hash){
        //LogUtils.d("TransactionViewModel===>","==timer=="+hash);
        BHBaseObserver<JsonObject> observer = new BHBaseObserver<JsonObject>(false) {
            @Override
            protected void onSuccess(JsonObject jsonObject) {
                TransactionOrder transactionOrder = JsonUtils.fromJson(jsonObject.toString(), TransactionOrder.class);
                //LoadDataModel ldm = new LoadDataModel(transactionOrder);
                //transLiveData.postValue(ldm);
                if(transactionOrder!=null && !TextUtils.isEmpty(transactionOrder.hash)){
                    SequenceManager.getInstance().getPeddingTxMap().remove(transactionOrder.hash);
                    //LogUtils.d("TransactionViewModel===>","==remove=="+hash+"==size=="+SequenceManager.getInstance().getPeddingTxMap().size());
                }
            }

            @Override
            protected void onFailure(int code, String errorMsg) {
                super.onFailure(code, errorMsg);
                //LoadDataModel ldm = new LoadDataModel(code,"");
                //transLiveData.postValue(ldm);
            }
        };
        BHttpApi.getService(BHttpApiInterface.class)
                .queryTranscationView(hash)
                .compose(RxSchedulersHelper.io_main())
                //.as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(activity)))
                .subscribe(observer);
    }

    public class PeddingTx{
        public String tx;
        public String label;
        public long time;


    }

    public class TranscationResponse{

        /**
         * height : 0
         * txhash : 4E7DC6ABC3323CAA54C1B1B30544633F5FA8C19F977C037594548145ED497256
         * raw_log : [{"msg_index":0,"success":true,"log":"","events":[{"type":"message","attributes":[{"key":"action","value":"send"}]}]}]
         * logs : [{"msg_index":0,"success":true,"log":"","events":[{"type":"message","attributes":[{"key":"action","value":"send"}]}]}]
         */

        public String height;
        public String txhash;
        public String raw_log;
        public List<LogsBean> logs;

        public  class LogsBean {
            /**
             * msg_index : 0
             * success : true
             * log :
             * events : [{"type":"message","attributes":[{"key":"action","value":"send"}]}]
             */

            public int msg_index;
            public boolean success;
            public String log;

        }
    }
}
