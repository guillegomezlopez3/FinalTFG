# Problema: Múltiples instancias de Java corriendo

## ¿Por qué pasa esto?

El problema de tener múltiples instancias de Java ejecutándose simultáneamente puede deberse a varias causas:

### 1. **Arranques múltiples desde IntelliJ IDEA**
- Al hacer clic en "Run" varias veces sin esperar a que termine la primera ejecución
- IntelliJ puede crear una nueva instancia sin cerrar la anterior si falla el arranque
- El hot-reload de DevTools puede dejar procesos huérfanos

### 2. **Maven en background no terminado**
- Si ejecutaste `mvnw.cmd spring-boot:run` y cerraste la terminal sin hacer `Ctrl+C`
- El proceso Java sigue corriendo en segundo plano
- Cada nuevo intento crea una instancia adicional

### 3. **Procesos Java huérfanos**
- Cuando la aplicación falla al arrancar (ej: puerto ocupado), puede dejar procesos sin terminar
- IntelliJ IDEA a veces no mata el proceso cuando detienes la ejecución manualmente

---

## ¿Cómo detectarlo?

### Ver todas las instancias de Java:
```powershell
Get-Process -Name java -ErrorAction SilentlyContinue | Select-Object Id, @{Name="Memory(MB)";Expression={[math]::Round($_.WorkingSet64 / 1MB, 2)}}, StartTime | Format-Table -AutoSize
```

### Ver qué usa el puerto 8081:
```powershell
netstat -ano | Select-String ":8081"
```

---

## ¿Cómo solucionarlo?

### Solución 1: Script automático (RECOMENDADO)
Ejecuta antes de cada arranque:
```powershell
.\cleanup-java.ps1
```

### Solución 2: Matar todos los procesos Java manualmente
```powershell
Stop-Process -Name java -Force
```

### Solución 3: Matar un proceso específico por PID
```powershell
# Primero identifica el PID
netstat -ano | Select-String ":8081"

# Luego mata ese proceso (cambia 12345 por el PID real)
Stop-Process -Id 12345 -Force
```

---

## ¿Cómo prevenirlo?

### ✅ Buenas prácticas:

1. **Antes de arrancar, siempre verifica:**
   ```powershell
   .\cleanup-java.ps1
   ```

2. **En IntelliJ IDEA:**
   - Usa el botón **Stop** (cuadrado rojo) antes de volver a ejecutar
   - Espera a ver "Started TfgFitAppApplication" antes de detener
   - No hagas clic múltiple en el botón Run

3. **En terminal con Maven:**
   - Usa `Ctrl+C` para detener limpiamente
   - Nunca cierres la terminal sin detener el proceso primero

4. **Configurar IntelliJ para matar procesos automáticamente:**
   - File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner
   - Marca: ✅ "Delegate IDE build/run actions to Maven"

5. **Habilitar un puerto aleatorio en desarrollo (opcional):**
   En `application.properties` puedes usar:
   ```properties
   server.port=${PORT:8081}
   ```
   Y luego arrancar con:
   ```powershell
   $env:PORT=8082; .\mvnw.cmd spring-boot:run
   ```

---

## Estado actual del sistema

Después de ejecutar `.\cleanup-java.ps1`, deberías tener:

✅ **0 instancias de Java corriendo**  
✅ **Puerto 8081 libre**  
✅ **Listo para arrancar sin conflictos**

---

## Si el problema persiste

1. **Reinicia IntelliJ IDEA** completamente
2. **Verifica que no tengas otras aplicaciones Java corriendo** (ej: Jenkins, Tomcat standalone)
3. **Cambia el puerto** a uno menos común:
   ```properties
   # application.properties
   server.port=9090
   ```

---

## Comando rápido de diagnóstico

Copia y pega esto para ver el estado completo:

```powershell
Write-Host "`n=== DIAGNÓSTICO TFGFITAPP ===" -ForegroundColor Cyan
Write-Host "`nInstancias de Java:" -ForegroundColor Yellow
Get-Process -Name java -ErrorAction SilentlyContinue | Select-Object Id, @{Name="Memory(MB)";Expression={[math]::Round($_.WorkingSet64 / 1MB, 2)}} | Format-Table
Write-Host "`nPuerto 8081:" -ForegroundColor Yellow
$port = netstat -ano | Select-String ":8081"
if($port) { $port } else { Write-Host "  ✅ LIBRE" -ForegroundColor Green }
Write-Host "`n========================" -ForegroundColor Cyan
```

