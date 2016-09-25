#NoEnv  ; Recommended for performance and compatibility with future AutoHotkey releases.
; #Warn  ; Enable warnings to assist with detecting common errors.
SendMode Input  ; Recommended for new scripts due to its superior speed and reliability.
SetWorkingDir, C:\Users\bunu\Desktop  ; Ensures a consistent starting directory.
CoordMode, Pixel,Screen
ImageSearch, FoundX, FoundY, 0,0, 1280,720, C:\Users\bunu\Desktop\opening.PNG
if ErrorLevel = 0
	ControlSend,TButton1,{Enter},Error
ImageSearch, FoundX, FoundY, 0,0, 1280,720, C:\Users\bunu\Desktop\closing.PNG
if ErrorLevel = 0
	ControlSend,TButton1,{Enter},Error
