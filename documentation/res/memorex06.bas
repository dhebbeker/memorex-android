'------------------------------------------------------------
'                 ATTINY26 ADC test file   D.H. 2005-01
'------------------------------------------------------------
$regfile = "at26def.dat"

' default the internal osc runs at 1 MHz
$crystal = 1000000

Dim Beep As Integer
Dim Gruen As Integer
Dim Gelb As Integer
Dim Rot As Integer
Dim Blau As Integer
Dim V As Byte
Dim Zufall As Integer
Dim Reihe(50) As Byte
Dim X As Byte
Dim Arbeit As Byte
Dim Y As Bit
Dim Z As Byte
Dim J As Byte


Declare Sub Green()
Declare Sub Yellow()
Declare Sub Red()
Declare Sub Blue()
Declare Sub Play()
Declare Sub Replay()
Declare Sub Zufall()
Declare Sub Random()

Config Pina.0 = Output
Config Pina.1 = Output
Config Pina.2 = Output
Config Pina.3 = Output
Config Pina.4 = Input
Config Pina.5 = Input
Config Pina.6 = Input
Config Pina.7 = Input
Config Pinb.3 = Input
Config Pinb.4 = Output

Config Adc = Single , Prescaler = Auto
Start Adc
Neu:
For X = 1 To 20
   Reihe(x) = 0
Next X
Arbeit = 1
For V = 5 To 20
   Green
   Yellow
   Red
   Blue
Next V
V = 5
Zufall


Do
   Gruen = Getadc(3)
   Gelb = Getadc(4)
   Rot = Getadc(5)
   Blau = Getadc(6)


   Play
   Replay
   Reihe(arbeit) = Zufall
   Arbeit = Arbeit + 1


Loop
Sub Green()
      Porta.0 = 1
      For Beep = 1 To 500
         Portb.6 = 1
         Waitus 1000
         Beep = Beep + V
         Portb.6 = 0
         Waitus 1000
      Next Beep
      Porta.0 = 0
End Sub
Sub Yellow()
      Porta.1 = 1
      For Beep = 1 To 400
         Portb.6 = 1
         Waitus 1500
         Beep = Beep + V
         Portb.6 = 0
         Waitus 1500
      Next Beep
      Porta.1 = 0
End Sub
Sub Red()
      Porta.2 = 1
      For Beep = 1 To 300
         Portb.6 = 1
         Waitus 2000
         Beep = Beep + V
         Portb.6 = 0
         Waitus 2000
      Next Beep
      Porta.2 = 0
End Sub
Sub Blue()
      Porta.3 = 1
      For Beep = 1 To 200
         Portb.6 = 1
         Waitus 2500
         Beep = Beep + V
         Portb.6 = 0
         Waitus 2500
      Next Beep
      Porta.3 = 0
End Sub
Sub Play()
   For X = 1 To 20
      If Reihe(x) <> 0 Then
         If Reihe(x) = 1 Then Green
         If Reihe(x) = 2 Then Yellow
         If Reihe(x) = 3 Then Red
         If Reihe(x) = 4 Then Blue
         Waitms 250
      End If
   Next X
End Sub
Sub Replay()
   Weiter2:
   Gruen = 0
   Gelb = 0
   Rot = 0
   Blau = 0
   Y = 0
   For X = 1 To 20
      If Reihe(x) <> 0 Then
         Y = 0
         While Y = 0
            Gruen = Getadc(3)
            Gelb = Getadc(4)
            Rot = Getadc(5)
            Blau = Getadc(6)
            If Gruen > 500 Then
               While Gruen > 500
                  Random
                  Gruen = Getadc(3)
               Wend
               Green
               Waitms 250
               Z = 1
               Y = 1
               Goto Weiter1
            End If
            If Gelb > 500 Then
                 While Gelb > 500
                     Random
                     Gelb = Getadc(4)
                 Wend
                 Yellow
                 Waitms 250
                 Z = 2
                 Y = 1
                 Goto Weiter1
            End If
            If Rot > 500 Then
               While Rot > 500
                  Random
                  Rot = Getadc(5)
               Wend
               Red
               Waitms 250
               Z = 3
               Y = 1
               Goto Weiter1
            End If
            If Blau > 500 Then
                 While Blau > 500
                     Random
                     Blau = Getadc(6)
                 Wend
                 Blue
                 Waitms 250
                 Z = 4
                 Y = 1
                 Goto Weiter1
            End If
         Wend
         Weiter1:
         If Reihe(x) = Z Then                               'GOOOOOOOOD
            Goto Weiter3
         End If
         If Reihe(x) <> Z Then                              'BAAAAAAAD
            Waitms 1000
            Porta.2 = 1
            For Beep = 1 To 200
               Portb.6 = 1
               Waitus 2500
               Beep = Beep + V
               Portb.6 = 0
               Waitus 2500
            Next Beep
            Waitms 100
            For Beep = 1 To 200
               Portb.6 = 1
               Waitus 2500
               Beep = Beep + V
               Portb.6 = 0
               Waitus 2500
            Next Beep
            Waitms 1000
            Porta.2 = 0
            Waitms 1000
            Play
            Waitms 1500
            For J = 2 To Arbeit
                Waitms 250
                Red
            Next J
            Wait 3
            Goto Neu
         End If
       End If
       Weiter3:
   Next X
   Waitms 500
   For Beep = 1 To 500
      Portb.6 = 1
      Waitus 1000
      Beep = Beep + V
      Portb.6 = 0
      Waitus 1000
   Next Beep
   Waitms 100
   For Beep = 1 To 500
      Portb.6 = 1
      Waitus 1000
      Beep = Beep + V
      Portb.6 = 0
      Waitus 1000
   Next Beep
   Waitms 750
   Schlecht:
End Sub
Sub Zufall()
   Zufall = 1
   Do
      Zufall = Zufall + 1
      If Zufall > 4 Then Zufall = 1
      Gruen = Getadc(3)
   Loop Until Gruen > 500
   Reihe(arbeit) = Zufall
   Arbeit = Arbeit + 1
   Waitms 750
End Sub
Sub Random()
   Zufall = Zufall + 1
   If Zufall > 4 Then Zufall = 1
End Sub
End