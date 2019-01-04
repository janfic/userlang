package com.janfic.isp2019

class Writer {
    private static File file
    private static PrintStream output
   
    public static void setFile(File f) {
        file = f
        output = new PrintStream(new File(f.name.replace(".txt",".groovy")))
    }
    
    public static void write(Map translation) {
        if(translation.type.equals("Component")) {
            writeComponent(translation)
        }
    }
    
    public static void writeComponent(Map translation) {
        output.println "import com.badlogic.ashley.core.Component;"
        output.println ""
        output.println "public class $translation.name implements Component {"
        output.println "\t"
        output.print "}"
    }
    public static void writeEntity(Map translation) {
        
    }
    public static void writeSystem(Map translation) {
        
    }
    public static void writePack(Map translation) {
        
    }
    public static void writeAsset(Map translation) {
        
    }
    public static void writeScript(Map translation) { //function
        
    }
}