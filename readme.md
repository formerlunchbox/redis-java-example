# Redis java交互 example

## 构建哨兵集群环境

redis/redis-stack-server redis的官网增强版本，不包含管理界面
普通版本的redis需要自己构建所需模块，也可根据需要查看redislabs下编译了所需模块的image。

```shell
# 注意事项 版本7.0
# 配置了aclfile 会导致 requirepass被忽略 所以修改密码请去acl文件
# bind指的是绑定的地址和网卡 而不是允许或禁止ip。简而言之：能通过哪扇 '门'进来，而'人'不归我管
# sentinel 的配置和一般redis不同

# 日志
# linux默认128 redis 默认配置 511 
# linux /etc/sysctl.conf 添加 met.core.somaxconn = 2048
# sysctl -p
WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
# conf文件夹如果没有权限 sentinel将无法写入新配置 会出现警告

# 先将compose中 配置修改至符合自身情况 再构建。
docker-compose up -d
# 查看日志 是否正常运行
```

