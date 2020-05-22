package com.bhex.wallet.common.config;

/**
 * created by gongdongyang
 * on 2020/3/4
 */
public class ARouterConfig {

    public static final String APP_MAIN_PAGE ="/app/main";

    /**
     * Common模块
     */
    public static final String COMMON_MAIN_PAGE ="/common/main";
    //二维码扫描
    public static final String Commom_scan_qr = "/Common/scan_qr";

    /**
     * 我的
     */
    public static final String MINE_MAIN_PAGE = "/my/main";
    //语言设置
    public static final String MY_LANGUAE_SET_PAGE = "/my/languageSet";
    //修改密码
    public static final String MY_UPDATE_PASSWORD = "/my/update/password";

    //汇率设置
    public static final String MY_Rate_setting = "/my/rate_set";

    //消息中心
    public static final String MY_Message = "/my/message";
    //识别设置
    public static final String MY_Recognition = "/my/Recognition";


    /**
     * 助记词模块
     */
    public static final String MNEMONIC_INDEX_PAGE = "/mnemonic/index";
    //托管单元管理
    public static final String MNEMONIC_TRUSTEESHIP_MANAGER_PAGE = "/mnemonic/trusteeship/manager";

    //托管单元创建成功页面
    public static final String TRUSTEESHIP_CREATE_OK_PAGE = "/mnemonic/trusteeship/success";
    //助记词备份
    public static final String MNEMONIC_BACKUP = "/mnemonic/backup";

    //托管单元创建
    public static final String TRUSTEESHIP_IMPORT_INDEX = "/mnemonic/trusteeship/index";

    //托管单元创建
    public static final String TRUSTEESHIP_MNEMONIC_FRIST= "/mnemonic/mnemonic/frist";

    //托管单元导入-KeyStore
    public static final String TRUSTEESHIP_IMPORT_KEYSTORE= "/mnemonic/import/keystore";

    //导出KeyStore
    public static final String TRUSTEESHIP_EXPORT_KEYSTORE = "/mnemonic/export/keystore";

    //托管单元导入
    public static final String TRUSTEESHIP_IMPORT_MNEMONIC = "/mnemonic/import/mnemonic";

    //私钥导入
    public static final String TRUSTEESHIP_IMPORT_PRIVATEKEY = "/mnemonic/import/privatekey";

    //私钥导出
    public static final String TRUSTEESHIP_EXPORT_PRIVATEKEY = "/mnemonic/export/privatekey";

    //私钥导出提醒
    public static final String TRUSTEESHIP_EXPORT_PRIVATEKEY_TIP = "/mnemonic/export/privatekey_tip";



    //资产
    public static final String Balance_Search = "/balance/search";

    //资产详情页
    public static final String Balance_Assets_Detail = "/balance/assets";

    //充币
    public static final String Balance_transfer_in = "/balance/transfer/in";

    //提币
    public static final String Balance_transfer_out = "/balance/transfer/out";

    //跨链地址生成页面
    public static final String Balance_cross_address = "/balance/cross_adress";

    //转账详情
    public static final String Balance_transcation_detail = "/balance/transcation/detail";
    public static final String Balance_transcation_view = "/balance/transcation/view";

    //代币发行
    public static final String Token_Release = "/token/release";

    //验证人详情页
    public static final String Validator_Detail = "/validator/detail";
    public static final String Do_Entrust = "/validator/do_entrust";

    public static final String Proposal_Detail = "/proposal/detail";
    public static final String Do_Pledge = "/proposal/do_pledge";
    public static final String Do_Veto = "/proposal/do_veto";
    public static final String Create_Proposal = "/proposal/create_proposal";


}
