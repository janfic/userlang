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
        config.addCompilationCustomizers(imports)
    }
    
    public static void setFile( File f ) {
        file = f
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
    }
    
    public static void makeBinding() {
        binding = new Binding()
        binding."${translation.name}" = { Closure body -> 
            body.fields = { Map map ->
                translation.fields = map
            }
            body.defaults = { Map map ->
                translation.defaults = map
            }
            body()
            return body
        }
        binding.component = {
            translation.type = "Component"
        }
        binding.pack = { String p ->
            translation.pack = p
        }
    }
    
    public static Map getTranslation() {
        return translation
    }
}