package gui;

import imgui.ImGui;
import imgui.app.Color;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Gui {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = null;

    /**
     * Pointer to the native GLFW window.
     */
    protected long handle;

    /**
     * Background color of the window.
     */
    protected final Color colorBg = new Color(.5f, .5f, .5f, 1);

    public Gui(long window){
        handle = window;
    }

    /**
     * Method to initialize application.
     *
     * @param config configuration object with basic window information
     */
    public void init(final Configuration config) {
        //initWindow(config);
        initImGui(config);
        imGuiGlfw.init(handle, true);
        imGuiGl3.init(glslVersion);
    }

    /**
     * Method to dispose all used application resources and destroy its window.
     */
    public void dispose() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        disposeImGui();
        disposeWindow();
    }

    /**
     * Method to initialize Dear ImGui context. Could be overridden to do custom Dear ImGui setup before application start.
     *
     * @param config configuration object with basic window information
     */
    private void initImGui(final Configuration config) {
        ImGui.createContext();
    }

    /**
     * Method called every frame, before calling {@link #process()} method.
     */
    private void preProcess() {
    }

    /**
     * Method called every frame, after calling {@link #process()} method.
     */
    private void postProcess() {
    }

    /**
     * Main application loop.
     */
    public void run() {
        startFrame();
        preProcess();
        process();
        postProcess();
        endFrame();
    }

    /**
     * Method to be overridden by user to provide main application logic.
     */
    private void process(){
        ImGui.setNextWindowPos(0,0);
        ImGui.setNextWindowSize(100,100);
        ImGui.begin("Window");
        ImGui.text("Hello, World!");
        ImGui.plotLines("Ahoj", new float[]{0, 1, 0, 2, 1, 3, 2, 1, 5, 0}, 10);
        ImGui.end();

        ImGui.begin("Hello2");
        ImGui.text("Hello, World!");
        ImGui.end();
    }

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    protected void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and swaps GLFW buffers to show an updated frame.
     */
    protected void endFrame() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    /**
     * Method to destroy Dear ImGui context.
     */
    protected void disposeImGui() {
        ImGui.destroyContext();
    }

    /**
     * Method to destroy GLFW window.
     */
    protected void disposeWindow() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    /**
     * @return pointer to the native GLFW window
     */
    public final long getHandle() {
        return handle;
    }

    /**
     * @return {@link Color} instance, which represents background color for the window
     */
    public final Color getColorBg() {
        return colorBg;
    }
}
