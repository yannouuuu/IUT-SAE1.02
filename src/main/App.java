/**
 * App.java
 * 
 * Point d'entrée principal pour l'application EcoManager.
 * Contient la méthode `main` qui initialise et démarre le jeu.
 *
 * Auteur(s) : Yann RENARD, Yanis MEKKI
 */

import extensions.File;

class App extends Program {

    final String NOM = "EcoManager";
    final String DESSIN = "ressources/art_ascii.txt";
    final int TEMPS_PAUSE = 500;

    void algorithm() {
        // Initialisation des variables de jeu (A EQUILIBRER)
        int tour = 1;
        int budget = 45000;
        int pollution = 20;
        int bonheur = 85;

        afficherArtASCII(DESSIN);
        println("==========");
        println(NOM);
        println("==========");
        println();
        print("Appuyer sur entrée pour commencer à jouer");
        readString();
        clearScreen();

        // // Boucle principale du jeu
        // while (budget >= /* valeur */ || pollution >= /* valeur */ || bonheur >= /* valeur */) {
        //     afficherEtatJeu(tour, budget, pollution, bonheur);
        //     tour++;
        // }
        println("Fin du jeu !");
    }

    // Affichage de l'état du jeu
    void afficherEtatJeu(int tour, int budget, int pollution, int bonheur) {
        println("-------------------------");
        println("Tour " + tour);
        println("Budget : " + budget + " €");
        println("Pollution : " + pollution + " %");
        println("Bonheur : " + bonheur + " %");
        println("-------------------------");
        println("Actions disponibles :");
        println("1. Construire une usine");
        println("2. Planter des arbres");
        println("3. Organiser un festival");
        println("-------------------------");
        print("Choisissez une action (1-3) : ");
    }

    // Affichage du contenu d'un fichier texte d'après son chemin
    void afficherArtASCII(String chemin) {
        File unTexte = newFile(chemin);

        // Stockage dans une variable de la ligne suivante dans le fichier
        while (ready(unTexte)) {
            println(readLine(unTexte));
        }
    }
}