package gui;

import imgui.*;
import imgui.app.Color;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class Gui {

    private static BottomMenu bottomMenu;
    private static PauseMenu pauseMenu;
    private static DevStatsWindow devStatsWindow;

    private static ImFont font;
    private static ImFont fontLarge;

    private static boolean captureMouse = false;

    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static String glslVersion = null;
    /**
     * Pointer to the native GLFW window.
     */
    protected static long handle;

    /**
     * Background color of the window.
     */
    protected static final Color colorBg = new Color(.5f, .5f, .5f, 1);

    /**
     * Method to initialize application.
     *
     * @param config configuration object with basic window information
     */
    public static void init(final Configuration config,long window) {
        handle = window;
        bottomMenu = new BottomMenu();
        pauseMenu = new PauseMenu();
        devStatsWindow = new DevStatsWindow();

        initImGui(config);
        font = ImGui.getIO().getFonts().addFontDefault();
        //font.setScale(2);
        imGuiGlfw.init(handle, true);
        imGuiGl3.init(glslVersion);
    }

    /**
     * Method to dispose all used application resources and destroy its window.
     */
    public static void dispose() {
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
    private static void initImGui(final Configuration config) {
        ImGui.createContext();
    }

    /**
     * Method called every frame, before calling {@link #process()} method.
     */
    private static void preProcess() {
    }

    /**
     * Method called every frame, after calling {@link #process()} method.
     */
    private static void postProcess() {
    }

    /**
     * Main application loop.
     */
    public static void run() {
        startFrame();
        preProcess();
        process();
        postProcess();
        captureMouse = ImGui.getIO().getWantCaptureMouse();
        endFrame();
    }

    /**
     * Method to be overridden by user to provide main application logic.
     */
    private static void process(){
        //ImGui.pushFont(font);
        bottomMenu.render();
        pauseMenu.render();
        devStatsWindow.render();
        RightClickMenu.render();
        //ImGui.popFont();
    }

    /**
     * Method called at the beginning of the main cycle.
     * It clears OpenGL buffer and starts an ImGui frame.
     */
    protected static void startFrame() {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    /**
     * Method called in the end of the main cycle.
     * It renders ImGui and swaps GLFW buffers to show an updated frame.
     */
    protected static void endFrame() {
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
    protected static void disposeImGui() {
        ImGui.destroyContext();
    }

    /**
     * Method to destroy GLFW window.
     */
    protected static  void disposeWindow() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
        Objects.requireNonNull(GLFW.glfwSetErrorCallback(null)).free();
    }

    /**
     * @return pointer to the native GLFW window
     */
    public static final long getHandle() {
        return handle;
    }

    /**
     * @return {@link Color} instance, which represents background color for the window
     */
    public static final Color getColorBg() {
        return colorBg;
    }

    public static boolean isCaptureMouse(){
        return captureMouse;
    }
}
