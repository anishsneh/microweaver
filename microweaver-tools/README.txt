configure tinyproxy
static ip for vm
give path of ca bundle /etc/kubernetes/controller-manager
	KUBE_CONTROLLER_MANAGER_ARGS="--service_account_private_key_file=/etc/pki/kube-apiserver/serviceaccount.key --root-ca-file=/etc/ssl/certs/ca-bundle.crt"
	
kubernetes-dashboard.yaml
	args:
          # Uncomment the following line to manually specify Kubernetes API server Host
          # If not specified, Dashboard will attempt to auto discover the API server and connect
          # to it. Uncomment only if the default does not work.
          - --apiserver-host=http://192.168.57.147:8080
        livenessProbe:
        
        
Grab self signed certificate from https://10.254.0.1/api/v1/namespaces?resourceVersion=0 (check url from coredns pod logs)
Added/appended self signed certificate to /etc/ssl/certs/ca-bundle.crt
Restart services & restart coredns

On restart need to restart registry and push images again

yum install -y bind-utils
		
docker run -p 5000:5000 registry
	
http://192.168.57.147:8080/ui
http://172.17.0.5:8180/swagger-ui.html

kubectl exec --namespace=microweaver-system microweaver-core-service-2618224193-5rs3w -- ls /bin

kubectl exec -it --namespace=kube-system coredns-3788349839-84c2p -- sh

kubectl exec -it --namespace=microweaver-system microweaver-registry-service-01-478431632-7zv80 -- /bin/bash

kubectl logs -f --namespace=microweaver-system microweaver-core-service-2618224193-qv2l5
kubectl logs -f --namespace=microweaver-system microweaver-registry-service-01-2292992418-01bm6

kubectl get endpoints --namespace=microweaver-system

kubectl create -f registry.yaml
kubectl get pods  --namespace=microweaver-system
kubectl delete -f registry.yaml
kubectl describe service --namespace=microweaver-system microweaver-core-service

kubectl get pods  --all-namespaces