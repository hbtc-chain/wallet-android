## 基础配置
### 打开配置文件 gradle.properties （工程根目录下）
```
//打包证书配置
STORE_PWD=jzagX4eQPHhUpyyqhFDu9hCF
KEY_PWD=jzagX4eQPHhUpyyqhFDu9hCF
KEY_ALIAS=bh_open_wallet
STORE_FILE=bh_open_wallet.jks

//applicatin_id配置
application_id = com.bluehelix.open.wallet

//域名配置
BHConstants.java文件
public static final String API_BASE_URL = "https://explorer.hbtcchain.io/";
public static final String MARKET_URL = "https://juswap.io";
```
### 多渠道打包命令
./gradlew clean assembleHbtcReleaseChannels -PchannelList=渠道名称

### 查看签名文件信息
keytool -v -list -keystore bh_open_wallet.jks