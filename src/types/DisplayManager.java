/**
 * DisplayManager.java
 * 
 * Classe utilitaire pour gérer l'affichage dans le terminal.
 * 
 * Responsabilités :
 * - Afficher l'état de la ville après chaque tour (budget, pollution, bonheur).
 * - Afficher les choix disponibles pour le joueur.
 * - Afficher les messages de fin de jeu (victoire/défaite).
 * 
 * Auteur(s) : Yann RENARD, Yanis MEKKI
 */

class DisplayManager {
    void afficherEtatJeu(int tour, int budget, int pollution, int bonheur) {
        println("-------------------------");
        println("Tour " + tour);
        println("Budget : " + budget + " €");
        println("Pollution : " + pollution + " %");
        println("Bonheur : " + bonheur + " %");
        println("-------------------------");
        println("Actions disponibles :");
        println("...");
        println("...");
        println("...");
        println("-------------------------");
        print("Choisissez une action (1-3) : ");
    }
}
