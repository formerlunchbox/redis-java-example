# Redis java交互 example

## 构建哨兵集群环境

redis/redis-stack-server redis的官方增强版本，不包含管理界面  
普通版本的redis需要自己构建所需模块，也可根据需要查看redislabs下编译了所需模块的image。

```shell
# 注意事项 版本7.4-rc2
# 配置了aclfile 会导致 requirepass被忽略 所以修改密码请去acl文件
# bind指的是绑定的地址和网卡 而不是允许或禁止ip。简而言之：能通过哪扇 '门'进来，而'人'不归我管
# sentinel 的配置和一般redis不同
# 配置protected-mode yes后， 必须配置redis的default用户密码

# 日志
# linux默认128 redis 默认配置 511 
# linux /etc/sysctl.conf 添加 met.core.somaxconn = 2048
# sysctl -p
WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.

# conf文件夹如果没有权限 sentinel将无法写入新配置 会出现警告.
# 简单处理方式 chmod 0777 ./config

# 先将compose中的配置修改再构建。
docker-compose up -d
# 查看日志 是否正常运行
```

## 创建与测试springboot工程

- jdk8对应springboot2  
- 使用jedis 而不是lettuce
- 注意springboot版本，一旦使用了springbootstarter的包，要查看其是否使用了高于springboot2的版本  
  redission的springbootstarter 3.36.0 版本就包含了springboot3。虽然可以运行，但却让springdataredis的redistemplate失效了
- redission的unlock需要特定频道权限，所以acl控制中记得放开相应权限
  
```shell
#redission 加锁
EVAL "if (redis.call('exists', KEYS[1]) == 0) or (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then redis.call('hincrby', KEYS[1], ARGV[2], 1); redis.call('pexpire', KEYS[1], ARGV[1]); return nil; end; return redis.call('pttl', KEYS[1]);" 1 lock 30000 "4f4b426c-9014-402e-8217-fef54dc31de2:1"

#redission 解锁
EVAL "local val = redis.call('get', KEYS[3]); if val ~= false then return tonumber(val); end; if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then return nil; end; local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); if (counter > 0) then redis.call('pexpire', KEYS[1], ARGV[2]); redis.call('set', KEYS[3], 0, 'px', ARGV[5]); return 0; else redis.call('del', KEYS[1]); redis.call(ARGV[4], KEYS[2], ARGV[1]); redis.call('set', KEYS[3], 1, 'px', ARGV[5]); return 1; end;" 3 lock "redisson_lock__channel:{lock}" "redisson_unlock_latch:{lock}:9ea271cf4ac9c2315003a2ae3a810810" "0" "30000" "4f4b426c-9014-402e-8217-fef54dc31de2:1" "PUBLISH"
# redisson_lock__channel redisson_unlock_latch
# 如果没有权限 则会报ERR ACL failure in script: No permissions to access a channel script

#附上简单的acl说明
user cp_user on >testCp ~* &* +@all
# ~* 所有key &* 所有频道 +@all 所有指令
```


