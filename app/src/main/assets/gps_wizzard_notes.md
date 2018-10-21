# Système de déplacement GPS

Mathieu Hannoun

## Introduction

Le but de cette partie est de créer un algorithme capable d'aiguiller le drone vers des **points d'intérêts** tout en évitant certaines zones données.

Nous nous basons sur les **champs de potentiel** de Arkins pour élaborer cet algorithmes.

### Champ de potentiel

Une bonne méthode pour gérer l'aiguillage du drone est de considérer chaque **point d'intérêt** comme générateur d'un **champ d'attraction** ou de **répulsion** comme ceci :

![http://urrg.eng.usm.my/images/bulletin/vol8_june/6a_june15.jpg](http://urrg.eng.usm.my/images/bulletin/vol8_june/6a_june15.jpg)

Contrairement à la gravité l'attraction du champ d'un **point d'attraction** est proportionnelle à la distance (et inversement pour les **points de répulsion**).

## Approche vectorielle

La première méthode qui nous viens à l'esprit quand l'implémentation du champ de potentiel est une fonction qui pour chaque **point d'intérêt** renverrais un **vecteur** par rapport à la position actuelle du drone.

Le **vecteur** donnant l'orientation et la vitesse à déployer serait donc la somme de tous les **vecteurs** générés par les points d'intérêts.



### Coordonnées GPS

Une vision simpliste de l'utilisation des cordonnées GPS serait de les utiliser comme des coordonnées cartésiennes en deux dimensions sur une surface plane.

![http://cdn.shopify.com/s/files/1/0293/8205/files/latitude_longitude_grande.gif?12060491192372930797](http://cdn.shopify.com/s/files/1/0293/8205/files/latitude_longitude_grande.gif?12060491192372930797)

Les problèmes ne se posent que lors du passage de longitude 180° à -180° qui est au milieu de l'océan atlantique. Pour les utilisations que nous aurons de ce drone (certainement uniquement en Île de France), nous pouvons considérer que ce problème ne se posera pas.

### Génération d'un vecteur

La génération d'un **vecteur** pour un **point d'intérêt** donnée est faite comme ceci :

$\vec{v}_{Intérêt} = \begin{pmatrix}  Longitude_{Point d'intéret} - Longitude_{Drone}  \\ Latitude_{Point d'interet} - Latitude_{Drone} \end{pmatrix}$

Cette méthode est uniquement utilisée uniquement pour les **points d'attractions**, les **points de répulsions** seront traités dans la partie suivante.

La génération du **vecteur de commande** est exprimé comme :

$\vec{v}_{Commande} = \sum{ \vec{v}_{Intérêt}} $ 

## Bibliographie

[Champ de potentiel](https://pdfs.semanticscholar.org/725e/fa1af22f41dcbecd8bd445ea82679a6eb7c6.pdf)