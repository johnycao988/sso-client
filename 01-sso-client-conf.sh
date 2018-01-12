#!/bin/sh
## delete
kubectl delete configmap sso-client-configmap -n test
## Create  
kubectl create configmap sso-client-configmap --namespace=test --from-file=./hosts
