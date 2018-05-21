## A powerful Java extension system
### Introduction
Douglas brings you a flexible API to build modular and extensible software. We can separate the functionality into *modules*, which provide internal feature management, and *plugins*, for extensions located in an external environment.

### Using the plugin system
#### Creating a plugin
The first thing you need for a plugin is a *main class* which extends `AbstractPlugin`.
To make organization and recognition easier, each plugin must have it's own manifest. It contains all the information needed about it and it's represented by a file named `plugin.xml` located in `/META-INF/`. It should look like this:
```
<plugin>
    <main>MyPluginMainClass</main>
    <packageId>my.organization.myplugin.mainclasspackage</packageId>

    <descriptor>
        <groupId>my.organization</groupId>
        <artifactId>myplugin</artifactId>
        <version>0.1.0</version>
        <name>MyPluginName</name>
    </descriptor>
    
    <dependencies>
        <dependency>
            <groupId>my.organization</groupId>
            <artifactId>myplugindependency</artifactId>
            <name>MyPluginDependencyName</name>
            <version>0.1.0</version>
        </dependency>
    </dependencies>
</plugin>
```
To cover the elements of the manifest:
* `plugin` is the root element.
* `main` specifies the main class name.
* `packageId` indicates the main class container package.
* `descriptor` specifies the descriptor information.
* `dependencies` is optional and specifies the list of dependencies.
* `dependencies/dependency` shows the descriptor information about a dependency.


#### Loading plugins
In order to start using the plugin system, you must create a **PluginManager**. The standard way of doing it is the following:

```
PluginManager pluginManager = Douglas.newPluginManager();
```

This creates a PluginManager with the default configuration, where you are able to register, locate and load plugins.
Next, you need to load your plugin JAR files from your file system

```
Path sourceDirectory = Paths.get("/dir/of/plugins");

pluginManager.load(sourceDirectory);
```

Finally, when you stop using the plugins you just unload them
```
pluginManager.unload();
```

#### Google Guice & dependency injection
The plugin system has special support for [Guice](https://github.com/google/guice). If your application is using it, you may want to change some things:
* Instantiate your plugins by using the dedicated *instantiation strategy* (`new GuiceInstantiationStrategy<>(guice injector)`)
* Integrate custom Guice modules into your dependency graph by using [SPI](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html).

#### Dependencies
As you have seen, to declare a *dependency* on another plugin you must add it to the manifest.
To retrieve an instance of a dependency, simply use `plugin.getDependencySystem().find(descriptor parameters)`

### Modules
#### Creating a module
To create a module, you only need a class that extends `AbstractModule`.

#### Properties
Douglas comes with a simple property API, [Camden](https://git.ill.fi/fred/camden). To understand how *properties* work, you should read it's documentation.
Each module has a `PropertyManager` for registration and location of *properties*.
To register properties on a module, you can:
* scan all the fields in the class by annotating the module type with `@Propertied`
* register each property field by annotating it with `@Propertied`
* using `propertyManager.registerDirectly(property)`

#### Toggleable modules
Modules with *enable*/*disable* capabilities should extend the `ToggleableModule` class. Toggleable modules must know when they are enabled or not, and change their behavior based on that.

#### Registering a module
To register a module, you first need to create a **ModuleManager**. You can create one by using
```
ModuleManager moduleManager = Douglas.newModuleManager();
```
This will create a new ModuleManager with the default configuration. You can also use `ModuleManager.builder(modules)`

#### Dependencies
If you want to mark another module as a dependency, simply annotate your module type with `@Requires(AnotherModule.class)`. To obtain the dependency's instance use `module.getDependencySystem().find(AnotherModule.class)`.