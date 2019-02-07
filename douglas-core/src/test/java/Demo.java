import com.github.foskel.douglas.Douglas;
import com.github.foskel.douglas.plugin.PluginManager;
import com.github.foskel.douglas.plugin.impl.StandardPluginManager;

import java.nio.file.Paths;

public final class Demo {
    public static void main(String[] args) {
        PluginManager pluginManager = Douglas.newPluginManager();

        pluginManager.loadSingle(Paths.get("C:\\Users\\F\\IdeaProjects\\t2\\out\\artifacts\\TestPlugin\\TestPlugin.jar"));
        pluginManager.unload();
    }
}
