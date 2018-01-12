#!/bin/sh
## delete
kubectl delete configmap sso-client-configmap -n elk
## Create  
kubectl create configmap sso-client-configmap --namespace=elk --from-file=./hosts
