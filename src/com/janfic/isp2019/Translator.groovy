package com.janfic.isp2019

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

/*
 *   Translates files of USER into Map objects that contain a translation into Groovy
 */
class Translator {
    
    //Tools
    private static File file
    private static Binding binding
    private static GroovyShell shell
    private static GroovyClassLoader loader
    private static CompilerConfiguration config
    private static ImportCustomizer imports
    
    //Translated Items
    private static Map translation = [name:"", type:"", pack:""]
    
    //Static initialization of Tools
    static {
        config = new CompilerConfiguration()
        imports = new ImportCustomizer()
        imports.addStarImports("com.badlogic.gdx.graphics.g2d","com.badlogic.gdx.files", "com.badlogic.gdx.math","com.badlogic.gdx.graphics")
        binding = new Binding()
        config.addCompilationCustomizers(imports)
    }
    
    /**
     *   Set the current USER file to be translated. Also resets the translation
     */
    public static void setFile( File f ) {
        file = f
        translation = [name:"",type:"", pack:""]
        translation.name = file.name.substring(0, file.name.lastIndexOf('.'))
    }
    
    /**
     *   Compiles the current USER file
     */
    public static void compile() {
        makeBinding()
        loader = new GroovyClassLoader(new GroovyClassLoader(), config) //ugh
        shell = new GroovyShell(loader, binding, config)
        shell.evaluate(file)
    }
    
    /**
     *   Sets up the current Binding, which is the main object in charge of the translation. Syntax, Structure, and Keywords are established here
     */
    public static void makeBinding() {
        
        //creates the 'component' keyword
        binding.component = {
            translation.type = "Component"
        }
        
        //creates the 'asset' keyword
        binding.asset = {
            translation.type = "Asset"
        }
        
        //creats the 'entity' keyword
        binding.entity = {
            translation.type = "Entity"
        }
        
        //creates the 'system' keyword
        binding.system = {
            translation.type = "System"
        }
        
        //creates a valid phrase for the name of the definition in USER
        binding."${translation.name}" = { Closure body -> 
            
            // creates the 'fields' keyword and its structure
            body.fields = { Map map ->
                translation.given = translation.fields = map
            }
            
            // makes 'given' have the same effect as 'fields'
            body.given = body.fields 
            
            //creates the 'run' keyword and its following required structure
            body.run = {Closure b -> 
                translation.body = extractScript()
            }
            
            //creates the 'defaults' keyword and its following required structure
            body.defaults = { Map map ->
                translation.defaults = map
            }
            
            //create the 'components' keyword and its following required structure
            body.components = { String... comps ->
                translation.components = comps
            }            
            
            body.family = { Map map ->
                translation.family = map
            }
            
            //runs the body
            body()
            return body
        }
        
        //creates the 'pack' keyword and required input
        binding.pack = { String p ->
            translation.pack = p
        }
    }
    
    /**
     *   Extracts the runnable script from the file by extracting it from the String version of the file.
     */
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
    
    /**
     *   Returns the translation
     */
    public static Map getTranslation() {
        return translation
    }
}