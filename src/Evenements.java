/**
 * Evenements.java
 * 
 * Représente un événement aléatoire pouvant survenir dans le jeu.
 * 
 * Attributs :
 * - Nom de l'événement.
 * - Effets sur les variables de jeu (argent, pollution, bonheur).
 * 
 * Responsabilités :
 * - Instanciation d'un événement à partir d'une ligne du fichier CSV.
 * - Gestion des effets de l'événement lorsqu'il est déclenché.
 * 
 * Auteurs : Yann RENARD, Yanis MEKKI
 */

class Evenements{
    String nom;
    String desc;
    int argent;
    int pollution;
    int bonheur;
}
