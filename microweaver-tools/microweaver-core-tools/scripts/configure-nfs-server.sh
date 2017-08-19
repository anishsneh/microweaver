sudo setsebool -P virt_use_nfs 1
sudo yum -y install nfs-utils libnfsidmap
sudo systemctl enable rpcbind nfs-server
sudo systemctl start rpcbind nfs-server rpc-statd nfs-idmapd
sudo mkdir -p /nfsfileshare
sudo chmod 777 /nfsfileshare/
sudo echo "/nfsfileshare 192.168.57.147(rw,sync)" >> /etc/exports
sudo exportfs -r