#!/bin/bash

#Connect instance and install httpd
ssh -i practicakey.pem ec2-user@ec2-34-205-72-66.compute-1.amazonaws.com ' sudo yum -y install httpd; 
sudo chmod 777 /var/; sudo chmod 777 /var/www;
sudo chmod 777 /etc/; sudo chmod 777 /etc/httpd; sudo chmod 777 /etc/httpd/conf; sudo chmod 777 /etc/httpd/conf/httpd.conf; 
sudo sed -i '"'"'s|DocumentRoot "/var/www/html"|DocumentRoot "/var/www"|'"'"' /etc/httpd/conf/httpd.conf;
sudo service httpd start';
scp -i practicakey.pem index.html ec2-user@ec2-34-205-72-66.compute-1.amazonaws.com:/var/www


