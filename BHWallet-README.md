### 多渠道打包命令
./gradlew clean assembleBHopReleaseChannels -PchannelList=渠道名称

### 查看签名文件信息
keytool -v -list -keystore bh_wallet.jks
