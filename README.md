## Projet OSproject

Projet scolaire consistant à développer une application web simulant un système d’exploitation simplifié, avec une interface graphique inspirée d’un bureau (desktop), un explorateur de fichiers et une corbeille.

L’objectif du projet était de manipuler des concepts proches d’un OS (explorateur, dossiers, fichiers, suppression/restauration) à travers une application web moderne, tout en appliquant les bonnes pratiques de développement frontend et backend.

## Contexte

- Projet réalisé dans le cadre de mes études
- Travail individuel

## Fonctionnalités

- Interface « bureau » (desktop) avec gestion de fenêtres
- Explorateur de fichiers avec navigation par dossiers
- Gestion hiérarchique des dossiers (relation parent / enfant)
- Affichage des fichiers à l’intérieur des dossiers
- Suppression logique des fichiers et dossiers (déplacement vers la corbeille)
- Corbeille dédiée affichant les éléments supprimés avec possibilité de suppression définitive
- Restauration des fichiers et dossiers depuis la corbeille
- Mise à jour dynamique de l’interface sans rechargement de page
- Gestion de l’état des fenêtres (ouvrir, fermer, réduire, agrandir)
- Consultation du contenu des fichiers (textes et images)
- Affichage des détails des fichiers (taille, date de création, etc.)
- Création de fichiers et de dossiers
- Breadcrumb interactif permettant la navigation par saisie du chemin

## Technologies utilisées

- Frontend : React / TypeScript / HTML / CSS
- Backend : Java / Spring Boot
- Base de données : MySQL
- Architecture : API REST, architecture en 4 couches (API, Application, Domain, Infrastructure)

## Ce que ce projet démontre

- Synchronisation frontend / backend sans rechargement de la page
- Modélisation d’une hiérarchie de données (dossiers / fichiers)
- Implémentation d’un système de corbeille (soft delete + restauration)
- Organisation du code et séparation des responsabilités
- Capacité à concevoir une interface inspirée d’un système d’exploitation réel

## Lancement du projet

Le projet est principalement destiné à être consulté dans le cadre d’une évaluation ou d’un portfolio, mais il est entièrement exécutable via Docker Compose.

## Prérequis
- Docker

Étapes

Cloner le dépôt :

```bash
git clone https://github.com/mykytamyronenko/OSproject.git
cd OSproject/OSproject
docker compose up --build
```
Le premier lancement peut prendre plusieurs minutes.

Une fois les conteneurs démarrés, ouvrir un navigateur web (Chrome recommandé) et accéder à :
```bash
http://localhost:5173
