package it.polimi.ingsw.Common.Utils;

public enum Printer {
     blueCard("[38;2;13;50;77;48;2;0;119;182m   "),
     purpleCard("[38;2;100;50;77;48;2;255;0;110m   "),
     whiteCard("[38;2;186;182;189;48;2;255;236;209m   "),
     greenCard("[38;2;122;199;79;48;2;106;153;69m   "),
     cyanCard("[38;2;162;210;255;48;2;0;180;216m   "),
     yellowCard("[38;2;247;206;91;48;2;232;170;20m   "),


     blueCardShelf("[38;2;13;50;77;48;2;0;119;182m B "),
     purpleCardShelf("[38;2;100;50;77;48;2;255;0;110m P "),
     whiteCardShelf("[38;2;186;182;189;48;2;255;236;209m W "),
     greenCardShelf("[38;2;122;199;79;48;2;106;153;69m G "),
     cyanCardShelf("[38;2;162;210;255;48;2;0;180;216m C "),
     yellowCardShelf("[38;2;247;206;91;48;2;232;170;20m Y "),


     blueCardNot("[38;2;0;0;0;48;2;0;119;182m X "),
     purpleCardNot("[38;2;0;0;0;48;2;255;0;110m X "),
     whiteCardNot("[38;2;0;0;0;48;2;255;236;209m X "),
     greenCardNot("[38;2;0;0;0;48;2;106;153;69m X "),
     cyanCardNot("[38;2;0;0;0;48;2;0;180;216m X "),
     yellowCardNot("[38;2;0;0;0;48;2;232;170;20m X "),

     blueCardDark("[38;2;0;0;0;48;2;0;119;182m * "),
     purpleCardDark("[38;2;0;0;0;48;2;255;0;110m * "),
     whiteCardDark("[38;2;0;0;0;48;2;255;236;209m * "),
     greenCardDark("[38;2;0;0;0;48;2;106;153;69m * "),
     cyanCardDark("[38;2;0;0;0;48;2;0;180;216m * "),
     yellowCardDark("[38;2;0;0;0;48;2;232;170;20m * "),

     shelfBackGorund("[48;2;164;113;72m   "),
     shelfSeparator("[48;2;96;56;8m "),
     shelfBase("[48;2;96;56;8m    "),

     whiteBlock("[48;2;255;255;255m   "),
     equalBlock("[38;2;225;6;0;48;2;255;236;209m = "),
     emptyBlock("[38;2;225;6;0;48;2;255;236;209m % "),
     dfrntBlock("[38;2;225;6;0;48;2;255;236;209m ≠ "),
     vLink("[38;2;0;0;0;48;2;255;255;255m | "),
     hLink( "[38;2;0;0;0;48;2;255;255;255m---"),
     whiteBackground("[38;2;0;0;0;48;2;255;255;255m"),
     trasparentText("[38;2;255;255;255;48;2;255;255;255m");

     static final String RESET = "[0m";
     private String escape;

    Printer(String escape) {
        this.escape = escape;
    }
    public String escape(){
        return escape;
    }
}
