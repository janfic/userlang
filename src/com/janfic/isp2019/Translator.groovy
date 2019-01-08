package com.janfic.isp2019

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

class Translator {
    private static File file
    private static Binding binding
    private static GroovyShell shell
    private static GroovyClassLoader loader
    private static CompilerConfiguration config
    private static ImportCustomizer imports
    
    //Translated Items
    private static Map translation = [name:"", type:"", pack:""]
    
    static {
        config = new CompilerConfiguration()
        imports = new ImportCustomizer()
        binding = new Binding()
        config.addCompilationCustomizers(imports)
    }
    
    public static void setFile( File f ) {
        file = f
        translation = [name:"",type:"", pack:""]
        translation.name = file.name.substring(0, file.name.lastIndexOf('.'))
    }
    
    public static void addImport(String alias, String name) {
        imports.addImport(alias, name)
    }
    
    public static void compile() {
        makeBinding()
        loader = new GroovyClassLoader(new GroovyClassLoader(), config) //ugh
        shell = new GroovyShell(loader, binding, config)
        shell.evaluate(file)
        println translation
    }
    
    public static void makeBinding() {
        binding.script = {
            translation.type = "Script"
        }
        binding.component = {
            translation.type = "Component"
        }
        binding."${translation.name}" = { Closure body -> 
            body.fields = { Map map ->
                translation.given = translation.fields = map
            }
            body.given = body.fields
            body.run = {Closure b -> 
                translation.body = extractScript()
            }
            body.defaults = { Map map ->
                translation.defaults = map
            }
            body()
            return body
        }
        
        binding.pack = { String p ->
            translation.pack = p
        }
    }
    
    public static String extractScript() {
        String contents = file.text
        int bracePairs = 0
        int first = contents.indexOf("run {") + 4
        int index = first
        while(bracePairs >= 0) {
            index++
            if(contents.charAt(index) == '{') {
                bracePairs++
            }
            else if(contents.charAt(index) == '}') {
                bracePairs--
            }
        }
        return contents.substring(first + 1, index).trim()
    }
    
    public static Map getTranslation() {
        return translation
    }
}