package org.hoohacks;

/**
 * This is required because JavaFX doesn't play well with Maven shade.
 */
public class SuperMain {
    public static void main(String[] args) {
        Main.main(args);
    }
}
