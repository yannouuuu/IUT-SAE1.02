<br/>
<p align="center">
    <picture>
        <source media="(prefers-color-scheme: dark)" srcset="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_light.png" width="200px">
        <img alt="UnivLilleLogo" src="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/header_univlille_dark.png" width="200px">
    </picture>
  <h1 align="center">IUT-SAE1.02 | EcoManager</h1>
</p>

<p align="center">
    Module d'initiation au développement iJava simple en BUT1 d'Informatique
    <br/>
    Comparaison d’approches algorithmiques
    - Logiciels Ludo-pédagogiques
    <br/>
    <br/>
    <a href="https://moodle.univ-lille.fr/course/view.php?id=30388&sectionid=262713"><strong>Voir la page sur le moodle »</strong></a>
    <br/>
    <br/>
    <a href="https://github.com/yannouuuu/IUT-SAE1.02/"><strong>Voir le projet complet sur GitHub »</strong></a>
</p>

<br/>

## Description du projet

**EcoManager** est un jeu ludo-pédagogique conçu pour initier à la gestion durable d'une ville. 
Le joueur incarne le maire d’une petite cité et prend des décisions impactant le **budget**, la **pollution** et le **bonheur des citoyens**.  
L’objectif : équilibrer ces trois paramètres pour construire une ville prospère tout en respectant l’environnement.  
Ce projet s'inscrit dans le cadre de la SAE 1.02 du BUT Informatique et vise à comparer des approches algorithmiques tout en développant une application interactive.

## Fonctionnalités : To-Do List

### **À faire absolument (version minimale) :**
- [ ] **Mode texte uniquement** pour le jeu.  
- [ ] Contrôle robuste de saisie pour éviter les erreurs/crashs.  
- [ ] Présentation de choix simples :  
  - [ ] Construire une usine.  
  - [ ] Planter des arbres.  
  - [ ] Organiser un festival (ou autre action sociale).  
- [ ] Gestion des ressources principales :  
  - [ ] Argent (budget).  
  - [ ] Pollution.  
  - [ ] Satisfaction des citoyens (bonheur).  
- [ ] Affichage clair de l’état de la ville après chaque tour :  
  - [ ] Points.  
  - [ ] Pollution.  
  - [ ] Population heureuse.  
- [ ] Détection des fins possibles :  
  - [ ] Victoire (budget positif, pollution maîtrisée <50%, citoyens heureux >70%).  
  - [ ] Défaite (pollution >100%, argent à 0, bonheur <50%).  

### **Envisagé (fonctionnalités bonus si le temps le permet) :**
- [ ] **Plus de types de décisions :**  
  - [ ] Subventionner les transports en commun.  
  - [ ] Taxer les industries.  
  - [ ] Actions spécifiques selon le contexte.  
- [ ] **Système d’événements aléatoires :**  
  - [ ] Grèves.  
  - [ ] Inondations.  
  - [ ] Manifestations écologistes.  
- [ ] Sauvegarde et chargement de la partie.  

### **Ambitieux (difficile à réaliser dans le délai) :**
- [ ] **Mode graphique basique** : carte ASCII représentant les zones de la ville.  
- [ ] Système de **"conseillers"** :  
  - [ ] Conseiller économique.  
  - [ ] Conseiller écologiste.  
  - [ ] Citoyens avec opinions variables.  
- [ ] **Mode multijoueur compétitif :**  
  - [ ] Comparaison des performances (scores ou villes).  
- [ ] Simulation avancée avec :  
  - [ ] Bâtiments spécifiques (écoles, hôpitaux, industries, etc.).  
  - [ ] Gestion fine des zones urbaines.  

## Environnement de développement

- **Langage :** Java, Ijava
- **Outils :**
  - IDE/Text Editor : IntelliJ IDEA, VS Code ou NeoVim
  - Git pour la gestion de version
- **Configuration minimale :**
  - JDK 11 ou supérieur

## Installation et Configuration

### 1. Prérequis
- **Java Development Kit (JDK)** version 11 ou supérieure.
- **Git** pour cloner le dépôt.

### 2. Étapes d'installation
```bash
# Cloner le dépôt
git clone https://github.com/yannouuuu/IUT-SAE1.02/

# Accéder au répertoire
cd IUT-SAE1.02/
```
3. Compilation et Exécution
```bash
# Compilation
./compile.sh

# Exécution
./run.sh
```

## Guide d'utilisation
1. *Lancer le jeu* : Exécutez la commande ```./run.sh``` après compilation.
2. *Menu principal* : Choisissez entre une nouvelle partie, charger une sauvegarde ou quitter.
3. *Tour de jeu* : Prenez des décisions via les options proposées (ex. : construire une usine, planter des arbres). // Observez les impacts sur les trois variables principales (Budget, Pollution, Bonheur).
4. *Fin de partie* : Atteignez la victoire en maintenant un équilibre après 10 tours ou évitez les conditions de défaite.

## Exemple de gameplay
Exemple d'affichage en cours de jeu
```bash
-------------------------
Tour 1
Budget : 45000 €
Pollution : 20 %
Bonheur : 85 %
-------------------------
Actions disponibles :
1. Construire une usine (-5000 €, +20% pollution, -10% bonheur)
2. Planter des arbres (-2000 €, -10% pollution, +15% bonheur)
3. Organiser un festival (-3000 €, +5% bonheur)
-------------------------
Choisissez une action (1-3) :
```

## Structure des fichiers
```plaintext
IUT-SAE1.02/
├── .github/
│   └── assets/
├── .vscode/
│   ├── extensions.json
│   ├── launch.json
│   └── settings.json
├── lib/
│   └── program.jar
├── ressources/
│   ├── decisions.csv            # Contient les choix disponibles et leurs impacts.
│   ├── evenements.csv           # Définit les événements aléatoires possibles.
│   └── configuration.csv        # Paramètres de départ (budget, pollution, bonheur).
├── shots/                       # Captures d'écran pour documenter le projet.
├── src/ 
│   ├── main/
│   │   ├── Main.java              # Point d'entrée principal du programme.
│   │   └── GameEngine.java        # Logique principale du jeu (boucle, gestion des tours).
│   ├── types/
│   │   ├── CityState.java         # Classe pour représenter l'état actuel de la ville.
│   │   ├── Configuration.java     # Classe pour stocker les paramètres initiaux du jeu.
│   │   ├── Decision.java          # Classe pour représenter une décision.
│   │   ├── DisplayManager.java    # Classe pour gérer l'affichage dans le terminal.
│   │   └── Evenement.java         # Classe pour représenter un événement.
├── .gitignore                   # Liste des fichiers/dossiers à ignorer par Git.
├── compile.sh                   # Script de compilation.
├── LICENSE                      # Licence du projet.
├── README.md                    # Documentation principale (ce fichier).
└── run.sh                       # Script pour lancer le projet.
```

## Captures d'écran
Des exemples visuels du fonctionnement du jeu sont disponibles dans le répertoire [shots](./shots).

## Développé avec 💖 par
- Yann RENARD
  > yann.renard.etu@univ-lille.fr
- Yanis MEKKI
  > yanis.mekki.etu@univ-lille.fr

---

### Remerciements

Nous tenons à créditer **SECQ Yann et ABIDI Sofiene, ALMEIDA COCO Amadeu, BONEVA Iovka, CASTILLON
Antoine, DELECROIX Fabien, LEPRETRE Éric, SANTANA MAIA Deise** pour la création des diaporamas de cours, des TP, TD et pour la réalisation des SAE. Leur travail a été précieux pour notre apprentissage.

<br/>
<p align="center">
    <picture>
        <img alt="UnivLilleLogo" src="https://github.com/yannouuuu/IUT-SAE1.01/raw/main/.github/assets/footer_univlille.png">
    </picture>
</p>
