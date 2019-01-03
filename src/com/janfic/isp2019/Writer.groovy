package com.janfic.isp2019

class Writer {
    private static File file
    private static PrintStream output
   
    public static void setFile(File f) {
        file = f
        output = new PrintStream(f)
    }
    
    public static void writeComponent(String name, String pack, Map variables) {
        
    }
    public static void writeEntity(String name, String pack, Map components) {
        
    }
    public static void writeSystem(String name, String pack, String[] family) {
        
    }
    public static void writePack(String name, String info, String displayName, String author, String[] components, String[] entities, String[] systems, String[] assets, String[] activeSystems ) {
        
    }
    public static void writeAsset(String name, String pack, String type, Closure script) {
        
    }
    public static void writeScript(String name) { //function
        
    }
}