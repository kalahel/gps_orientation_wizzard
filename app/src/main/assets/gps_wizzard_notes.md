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

### Génération de vecteur

#### Attraction

La génération d'un **vecteur** pour un **point d'intérêt** donnée est faite comme ceci :

$\vec{v}_{Intérêt} = \begin{pmatrix}  Longitude_{Point d'intéret} - Longitude_{Drone}  \\ Latitude_{Point d'interet} - Latitude_{Drone} \end{pmatrix}$

Cette méthode est uniquement utilisée uniquement pour les **points d'attractions**, les **points de répulsions** seront traités dans la partie suivante.

La génération du **vecteur de commande** serait exprimé comme :

$\vec{v}_{Commande} = \sum{ \vec{v}_{Intérêt}} $ 

Finalement nous n'exprimerons pas le **vecteur de commande** de cette façon car en réalité nous traiterons les points d'intérêts un par un. Il n'a pas de sens à vouloir à aller à tous les points en même temps.

Nous allons donc traiter les vecteurs d'intérêt un à un en allant à chaque fois au plus près.



![Orientation à multi destination](http://image.noelshack.com/fichiers/2018/47/5/1542971942-multidestinationorientation.png)



Les points seront donc visité dans l'ordre ${P1,P3,P2}$.

Cette méthode est évidemment non optimisé, il nous restera à implémenter un **algorithme d'optimisation** de parcours dans un seconds temps.

#### Répulsion

Plusieurs méthodes ont été envisagées pour les vecteurs de répulsions :

##### Fonction linéaire

Une première approche est de généré un vecteur de répulsion tel que :

$\vec{v_{Répulsion}} = \begin{pmatrix}  \frac{-1}{Longitude_{Point d'intéret} - Longitude_{Drone}}  \\ \frac{-1}{Latitude_{Point d'interet} - Latitude_{Drone}} \end{pmatrix}$

Pour le rendre inversement proportionnel à sa distance.

Le **vecteur de commande** deviendrait donc :

$\vec{v}_{Commande} =\vec{v}_{Intérêt} + \sum{ \vec{v}_{Répulsion}} $ 



![Interaction répulsion](http://image.noelshack.com/fichiers/2018/48/1/1543227818-repulsion1.png)



Le problème avec cette méthode viens du fait que **l'attraction** à un point étant proportionnelle à la distance,  sa norme pourrait vite devenir énorme et "écraser" celle du **vecteur de répulsion**.

Par exemple :

![Problèmes liées à la répulsion](http://image.noelshack.com/fichiers/2018/48/1/1543228544-pbrepulsion.png)



On aurait donc une **attraction** de $10$ en et une répulsion de $\frac{-1}{5}$ on peut même calculer  que la répulsion maximum serait de $-1$.

Le **drone** passerait donc par la zone à éviter sans la contourner.

##### Fonction gaussienne

Une solution plus élégante serait d'utiliser une fonction Gaussienne pour générer le vecteur de répulsion.

![Gaussienne](https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Gauss_reduite.svg/667px-Gauss_reduite.svg.png)

 Soit $f(x)=ae^{-{\frac {(x-b)^{2}}{2c^{2}}}}$ avec $x$ : représentant la distance.

Evidement l'objectif étant que plutôt que de culminer à $0.4$ la fonction tendrais en $0$ vers l'infinis :

$\lim\limits_{x\rightarrow0} f(x) = +\infty$

##### Implémentation actuelle



## Bibliographie

[Champ de potentiel](https://pdfs.semanticscholar.org/725e/fa1af22f41dcbecd8bd445ea82679a6eb7c6.pdf)