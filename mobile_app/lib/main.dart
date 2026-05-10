import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'providers/auth_provider.dart';
import 'providers/user_provider.dart';
import 'providers/diet_provider.dart';
import 'providers/workout_provider.dart';
import 'providers/chat_provider.dart';
import 'providers/progress_provider.dart';
import 'ui/screens/auth/login_screen.dart';
import 'ui/screens/main_scaffold.dart';
import 'utils/theme.dart';

/// Punto de entrada de la aplicación.
/// 
/// Inicializa el árbol de proveedores de estado y lanza la aplicación.
void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => UserProvider()),
        ChangeNotifierProvider(create: (_) => DietProvider()),
        ChangeNotifierProvider(create: (_) => WorkoutProvider()),
        ChangeNotifierProvider(create: (_) => ChatProvider()),
        ChangeNotifierProvider(create: (_) => ProgressProvider()),
      ],
      child: const FitAppMobile(),
    ),
  );
}

/// Clase principal de la aplicación Flutter.
/// 
/// Configura el tema global, el título y el widget inicial (AuthWrapper).
class FitAppMobile extends StatelessWidget {
  const FitAppMobile({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'LevelUp',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      home: const AuthWrapper(),
    );
  }
}

/// Widget que decide qué pantalla mostrar según el estado de autenticación.
/// 
/// Muestra la pantalla de carga, el panel principal o el inicio de sesión.
class AuthWrapper extends StatelessWidget {
  const AuthWrapper({super.key});

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);

    if (authProvider.status == AuthStatus.authenticating) {
      return const Scaffold(
        body: Center(
          child: CircularProgressIndicator(),
        ),
      );
    }

    if (authProvider.status == AuthStatus.authenticated) {
      return const MainScaffold();
    }

    return const LoginScreen();
  }
}

