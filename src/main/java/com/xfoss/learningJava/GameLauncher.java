package com.xfoss.learningJava;

import com.sun.jna.Function;
import com.sun.jna.platform.win32.WinDef.BOOL;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinDef.DWORDByReference;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class GameLauncher {
    private static void enableWindows10AnsiSupport() {        
        Function GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");       
        DWORD STD_OUTPUT_HANDLE = new DWORD(-11);       
        HANDLE hOut = (HANDLE) GetStdHandleFunc.invoke(HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});        
        DWORDByReference p_dwMode = new DWORDByReference(new DWORD(0));       
        Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");       
        GetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, p_dwMode});        
        int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;       
        DWORD dwMode = p_dwMode.getValue();       
        dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);       
        Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");       
        SetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, dwMode});   
    }
    
    public static void main (String[] args) {

        if (System.getProperty("os.name").startsWith("Windows"))
            enableWindows10AnsiSupport();

        GuessGame game = new GuessGame();
        game.startGame();
    }
}
