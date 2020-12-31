package com.bhex.wallet.common.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bhex.wallet.common.db.dao.BHTokenDao;
import com.bhex.wallet.common.db.dao.BHUserTokenDao;
import com.bhex.wallet.common.db.dao.BHWalletDao;
import com.bhex.wallet.common.db.entity.BHUserToken;
import com.bhex.wallet.common.db.entity.BHWallet;
import com.bhex.wallet.common.model.BHToken;

/**
 * Created by BHEX.
 * User: gdy
 * Date: 2020/3/4
 * Time: 22:22
 */
@Database(entities = {BHWallet.class, BHToken.class, BHUserToken.class} , version = 4,exportSchema = false)
public abstract  class AppDataBase extends RoomDatabase {

    private static final String DB_NAME ="bh_db";


    private static volatile AppDataBase INSTANCE;

    public abstract BHWalletDao bhWalletDao();
    public abstract BHTokenDao  bhTokenDao();
    public abstract BHUserTokenDao bhUserTokenDao();

    public static AppDataBase getInstance(Application context){
        if(INSTANCE==null){
            synchronized (AppDataBase.class){
                if(INSTANCE==null){
                    INSTANCE = buildDatabase(context);
                }
            }
        }
        return INSTANCE;
    }

    private static AppDataBase buildDatabase(Application context) {
        return Room.databaseBuilder(context,AppDataBase.class,DB_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        })
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_2_3)
                //.addMigrations(MIGRATION_3_4)
                .build();
    }

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `tab_token` (`p_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `symbol` TEXT, `issuer` TEXT, `chain` TEXT, `type` INTEGER NOT NULL, `is_send_enabled` INTEGER NOT NULL, `is_deposit_enabled` INTEGER NOT NULL, `is_withdrawal_enabled` INTEGER NOT NULL, `decimals` INTEGER NOT NULL, `total_supply` TEXT, `collect_threshold` TEXT, `deposit_threshold` TEXT, `open_fee` TEXT,`collect_fee` TEXT, `sys_open_fee` TEXT, `withdrawal_fee` TEXT, `max_op_cu_number` INTEGER DEFAULT 0, `systransfer_amount` TEXT, `op_cu_systransfer_amount` TEXT, `is_native` INTEGER NOT NULL, `custodian_amount` TEXT, `logo` TEXT);");
            database.execSQL("CREATE UNIQUE INDEX `index_tab_token_symbol` ON `tab_token` (`symbol`);");
            //database.execSQL("CREATE TABLE IF NOT EXISTS `tab_token` (`p_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `symbol` TEXT, `issuer` TEXT, `chain` TEXT, `type` INTEGER NOT NULL, `is_send_enabled` INTEGER NOT NULL, `is_deposit_enabled` INTEGER NOT NULL, `is_withdrawal_enabled` INTEGER NOT NULL, `decimals` INTEGER NOT NULL, `total_supply` TEXT, `collect_threshold` TEXT, `deposit_threshold` TEXT, `open_fee` TEXT, `sys_open_fee` TEXT, `withdrawal_fee` TEXT, `max_op_cu_number` INT default 0, `systransfer_amount` TEXT, `op_cu_systransfer_amount` TEXT, `is_native` INTEGER NOT NULL, `custodian_amount` TEXT, `logo` TEXT)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `tab_user_token` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `address` TEXT, `symbol` TEXT)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `temp` as select `id`,`address`,`name`,`keystorePath`,`mnemonic`,`isBackup`,`isDefault`,`publicKey` from tab_wallet");
            database.execSQL("drop table tab_wallet ");
            database.execSQL("alter table `temp` rename to  tab_wallet ");
        }
    };
}
