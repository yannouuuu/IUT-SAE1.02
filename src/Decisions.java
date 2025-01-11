/**
 * Decision.java
 * 
 * Représente une décision possible dans EcoManager.
 * 
 * Attributs :
 * - Description de la décision.
 * - Impacts sur les variables de jeu : argent, pollution, bonheur.
 * 
 * Responsabilités :
 * - Instanciation d'une décision à partir d'une ligne du fichier CSV.
 * 
 * Auteurs : Yann RENARD, Yanis MEKKI
 */

class Decisions{
    String nom;
    String desc;
    int argent;
    int pollution;
    int bonheur;
    String message;
}