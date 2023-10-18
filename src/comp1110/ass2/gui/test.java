package comp1110.ass2.gui;

import javafx.stage.Stage;

public class test {
    public static String rotateAssam(String currentAssam, int rotation) {
        rotation %= 180;
        if (rotation != 90 && rotation != -90 ) {
            return currentAssam; // Invalid rotation
        }

        char x = currentAssam.charAt(1);
        char y = currentAssam.charAt(2);
        char direction = currentAssam.charAt(3);

        switch (direction) {
            case 'N':
                direction = rotation == 90 ? 'E' : 'W' ;
                break;
            case 'E':
                direction = rotation == 90 ? 'S' : 'N' ;
                break;
            case 'S':
                direction = rotation == 90 ? 'W' : 'E' ;
                break;
            case 'W':
                direction = rotation == 90 ? 'N' : 'S' ;
                break;
        }

        return "A" + x + y + direction;
    }
    public static void main(String[] args){
        //String currentGame = "Pc04000iPy03900iPp01101iPr03001iA10EBy21c23c23y24r13c16c20n00p16c30c30r29r21c20r18r18y11y11c29r21r20n00n00y22c26y15p15p15r05r05y22r28r28y20r15n00c28c28c13p20p20r15n00n00c05c11y12p02r10";
        System.out.println(rotateAssam("A25S",270));
    }
}
