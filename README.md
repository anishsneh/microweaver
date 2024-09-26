# Microweaver
Microweaver is a scalable microservice orchestration framework built on Spring Boot, Kubernetes, MySQL &amp; RabbitMQ.
This repository includes core framework, service discovery, load balancing &amp; samples services, maven plugin configurations for development. It also includes Vagrant &amp; Ansible based development environment definitions to start with development cluster. | [http://www.anishsneh.com](http://www.anishsneh.com)

### Components
* Container Infrastructure
	* Kubernetes Platform
	* Kubernetes Dashboard
	* CAdvisor
	* Core DNS
	* Docker Distribution Service
	* Tinyproxy
* External Services
	* MariaDB
	* RabbitMQ
* Platform Services
	* Admin Service (microservice admin console based on Spring Boot Admin)
	* Registry Service (a service registry based on Netflix Eureka)
	* Gateway Service (a common service gateway  to access all deployed services based on Netflix Zuul)
	* Core Service (a service to deploy containers in Kubernetes on the fly based on Spring Boot micoservices)
	* Sidecar Service (a service to support python code incase required in platform services)
* Sample Services
	* HelloWorld Service
* Development Environment
	* Vagrant configuration
	* Ansible playbook. 

### Devbox Setup
##### Prerequisites
To spin development environment we need to install following tools on development machine (local desktop/laptop):
1. [Vagrant](https://www.vagrantup.com)
2. [Ansible](https://www.ansible.com)
3. [Virtualbox](https://www.virtualbox.org)
4. [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
5. [Maven](https://maven.apache.org/download.cgi)
6. [Git](https://en.wikipedia.org/wiki/Git)
7. [Docker](https://www.docker.com)
8. [Python (additional modules - pip, wheel, kubernetes)](https://www.python.org)
It needs atleast 4 CPUs &amp; 8GB memory for development VM.
> Note that we are referring local desktop/laptop as devbox &amp; kube cluster node as kubehost. The devbox MUST BE a Linux/Mac based machine, as of now Ansible on Windows is not supported (SELinux should be disabled on devbox).
##### Steps
1. Clone [Microweaver Repository](https://github.com/anishsneh/microweaver) from GitHub:
    ```sh
    [root@devbox ~]# git clone https://github.com/anishsneh/microweaver.git
    ```
2. Got to microweaver directory:
	```sh
    [root@devbox ~]# cd microweaver/microweaver-tools/microweaver-core-tools/devbox
    ```
3. Trigger development cluster installation using Vagrant:
	```sh
    [root@devbox ~]# vagrant up
    ```
	This may take upto few minutes. Once Vagrant VM is up & running we should have our Kubernetes platform ready to bootstrap platform service.
4. Verify development cluster:
	```sh
    [root@devbox ~]# vagrant ssh
    ```
5. Check if all platform services are running (wait until services have been deployed, up & running):
	```sh
    [root@devbox ~]# vagrant ssh
	Last login: Mon Aug 21 15:28:55 2017 from 10.0.2.2
	[vagrant@kubehost ~]$
	[vagrant@kubehost ~]$ sudo bash
    ```
    Check services:
	```sh
	[vagrant@kubehost ~]# for SERVICE in tinyproxy mariadb rabbitmq-server docker-distribution etcd kube-apiserver kube-controller-manager kube-scheduler kube-proxy kubelet docker; do systemctl status $SERVICE; done
    ```
    Check pods, you should see following two pods running:
	```sh
	[vagrant@kubehost ~]# kubectl get pods --namespace=kube-system
	NAME                                    READY     STATUS    RESTARTS   AGE
	coredns-3788349839-ftpzd                1/1       Running   0          3m
	kubernetes-dashboard-3839207111-5bqvm   1/1       Running   0          3m
    ```
6. Add insecure registry 192.168.57.150:5000 to docker configurations &amp; start local docker service (on devbox i.e. local desktop/laptop).
7. Build microweaver components &amp; deploy to development cluster's docker registry:
	```sh
    [root@devbox ~]# cd microweaver
    ```
    ```sh
    [root@devbox ~]# mvn clean deploy -Pdocker
    ```
    You should see following build results:
    ```sh
    [root@devbox ~]# [INFO] Reactor Summary:
	[INFO]
	[INFO] microweaver ........................................ SUCCESS [  1.233 s]
	[INFO] microweaver-platform ............................... SUCCESS [  0.120 s]
	[INFO] microweaver-core-platform .......................... SUCCESS [02:37 min]
	[INFO] microweaver-services ............................... SUCCESS [  0.160 s]
	[INFO] microweaver-registry-service ....................... SUCCESS [ 17.973 s]
	[INFO] microweaver-admin-service .......................... SUCCESS [ 11.772 s]
	[INFO] microweaver-gateway-service ........................ SUCCESS [ 13.970 s]
	[INFO] microweaver-core-service ........................... SUCCESS [ 20.922 s]
	[INFO] microweaver-sidecar-service ........................ SUCCESS [ 27.171 s]
	[INFO] microweaver-helloworld-service ..................... SUCCESS [ 13.101 s]
	[INFO] microweaver-samples ................................ SUCCESS [  0.150 s]
	[INFO] microweaver-services ............................... SUCCESS [  0.144 s]
	[INFO] microweaver-core-tools ............................. SUCCESS [  0.400 s]
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD SUCCESS
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 04:26 min
	[INFO] Finished at: 2017-08-21T15:51:12+01:00
	[INFO] Final Memory: 83M/479M
	[INFO] ------------------------------------------------------------------------
    ```
8. Check development cluster's docker registry for published images (images shown below should be available):
	```sh
    [root@devbox ~]# vagrant ssh
	Last login: Mon Aug 21 15:28:55 2017 from 10.0.2.2
	[vagrant@kubehost ~]$
	[vagrant@kubehost ~]$ sudo bash
	[vagrant@kubehost vagrant]#
	[vagrant@kubehost vagrant]# curl -X GET http://localhost:5000/v2/_catalog | python -m json.tool
	  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
	                                 Dload  Upload   Total   Spent    Left  Speed
	100   289  100   289    0     0  28052      0 --:--:-- --:--:-- --:--:-- 32111
	{
	    "repositories": [
	        "microweaver/base",
	        "microweaver/microweaver-admin-service",
	        "microweaver/microweaver-core-service",
	        "microweaver/microweaver-gateway-service",
	        "microweaver/microweaver-helloworld-service",
	        "microweaver/microweaver-registry-service",
	        "microweaver/microweaver-sidecar-service"
	    ]
	}
    ```
9. Set MICROWEAVER_HOME &amp; bootstrap microweaver system (in following example assuming repository was cloned into /home/anishsneh/microweaver):
	```sh
    [root@devbox ~]# export MICROWEAVER_HOME="/home/anishsneh/microweaver"
    [root@devbox ~]# python xbin/bootstrap.py
    ```
10. After successful bootstrapping SSH to kubehost again:
	```sh
    [root@devbox ~]# vagrant ssh
	Last login: Mon Aug 21 16:20:05 2017 from 10.0.2.2
	[vagrant@kubehost ~]$
	[vagrant@kubehost ~]$ sudo bash
    ```
    Check microweaver create pods, you should see following pods under creation (or recently created):
	```sh
	[root@devbox ~]# kubectl get pods --namespace=microweaver-system
	NAME                                               READY     STATUS    RESTARTS   AGE
	microweaver-admin-service-1572664947-jmwx2         1/1       Running   0          12s
	microweaver-core-service-3451778194-4bm3p          1/1       Running   0          12s
	microweaver-gateway-service-1848900354-6x3r7       1/1       Running   0          12s
	microweaver-registry-service-01-2042569877-l4dz9   0/1       Running   0          13s
	microweaver-registry-service-02-2548704405-3tpt8   0/1       Running   0          12s
	microweaver-sidecar-service-218452119-5fsl5        0/1       Running   0          11s
    ```
    Wait for few mins, eventually all the pods should be up &amp; running:
    ```sh
	[root@devbox ~]# kubectl get pods --namespace=microweaver-system
	NAME                                               READY     STATUS    RESTARTS   AGE
	microweaver-admin-service-1572664947-jmwx2         1/1       Running   0          2m
	microweaver-core-service-3451778194-4bm3p          1/1       Running   0          2m
	microweaver-gateway-service-1848900354-6x3r7       1/1       Running   0          2m
	microweaver-registry-service-01-2042569877-l4dz9   1/1       Running   0          2m
	microweaver-registry-service-02-2548704405-3tpt8   1/1       Running   0          2m
	microweaver-sidecar-service-218452119-5fsl5        1/1       Running   0          2m
    ```
11. From local browser navigate to http://192.168.57.150:8080/ui &amp; check Kubernetes dashboard, you should see microweaver platform pods deployed:
![Kubernetes Dashboard | http://www.anishsneh.com](https://user-images.githubusercontent.com/5123162/29532785-bc6cb8b2-86a6-11e7-8b7d-ddd50007860d.png)
12. From local browser navigate to http://192.168.57.150:30090 &amp; check Spring Boot Admin console, you should see microweaver platform services deployed:
![Admin Service | http://www.anishsneh.com](https://user-images.githubusercontent.com/5123162/30037663-02a29482-91b5-11e7-8ad4-527b068f7df5.png)
13. From local browser navigate to http://192.168.57.150:30080/api/core-service/swagger-ui.html &amp; check docs, you should see API docs:
![API Docs | http://www.anishsneh.com](https://user-images.githubusercontent.com/5123162/30491840-8aec3db4-9a36-11e7-83fc-2979dbba2072.png)
### Information
| Service/Component Name | Port | URL/Context
| ------ | ------ | ------ |
| Kubernetes Dashboard | 8080 | http://192.168.57.150:8080/ui |
| Kubernetes CAdvisor | 4194 | http://192.168.57.150:4194 |
| Maria DB | 3306 | jdbc:mysql://192.168.57.150:3306 |
| Rabbit MQ | 15672 | http://192.168.57.150:15672 |
| Microweaver Admin Service | 30090 | http://192.168.57.150:30090|
| Microweaver Service Gateway | 30080 | http://192.168.57.150:30080/api/** |
| Microweaver Core Service | 30080 | http://192.168.57.150:30080/api/core-service/v1.0/services/1 | 
| Microweaver Core Service API Docs | 30080 | http://192.168.57.150:30080/api/core-service/swagger-ui.html | 

> All services are accessible through gateway. On deployment services will dynamically register themselves with Eureka which in turn will be discovered by Zuul gateway &amp; corresponding route will be created. 
For service to be discoverable you MUST USE following application properties:
```sh
eureka.instance.hostname=${MICROSERVICE_SERVICE_NAME}
eureka.instance.preferIpAddress=true
eureka.client.serviceUrl.defaultZone=${SYSTEM_REGISTRY_DEFAULT_ZONE}
```
> Note that development cluster is all-in-one Kubernetes cluster (master & minion on the same node) with RabbitMQ &amp; MariaDB services (based on CentOS 7). The ip address of development cluster is 192.168.57.150 by default, it is configurable via Vagrantfile (if required).
