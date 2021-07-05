package gui;

import imgui.*;
import imgui.app.Color;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;
import window.gameStates.GameState;

public class Gui {

    private static BottomMenu bottomMenu;
    private static PauseMenu pauseMenu;
    private static DevStatsWindow devStatsWindow;

    public static ImFont font;
    public static ImFont font2;
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

        initImGui(config);
        font = ImGui.getIO().getFonts().addFontFromFileTTF("src/main/resources/fonts/Semi-Coder-Regular.otf",24);
        font2 = ImGui.getIO().getFonts().addFontFromFileTTF("src/main/resources/fonts/SimplySquare.ttf",48);
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
     * Main application loop.
     */
    public static void run(GameState state) {
        startFrame();
        //process();
        //ImGui.pushFont(font);
        state.gui();
        //ImGui.popFont();
        captureMouse = ImGui.getIO().getWantCaptureMouse();
        endFrame();
    }

    /**
     * Method to be overridden by user to provide main application logic.
     */
    private static void process(){
        //ImGui.pushFont(font);
        BottomMenu.render();
        PauseMenu.render();
        DevStatsWindow.render();
        RightClickMenu.render();
        EntityDetailWindow.render();
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

    public static boolean isCaptureMouse(){
        return captureMouse;
    }
}
