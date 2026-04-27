import 'package:flutter/material.dart';
import 'utils/theme.dart';
void main() {
  runApp(const FitAppMobile());
}
class FitAppMobile extends StatelessWidget {
  const FitAppMobile({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'TFG FitApp',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      home: const DemoScreen(),
    );
  }
}
class DemoScreen extends StatelessWidget {
  const DemoScreen({super.key});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('TFG FitApp'),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Bienvenido a TFG FitApp',
                style: Theme.of(context).textTheme.titleLarge,
              ),
              const SizedBox(height: 16),
              const Card(
                child: Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Text('La tarjeta usa los mismos colores que la web.'),
                ),
              ),
              const SizedBox(height: 24),
              ElevatedButton(
                onPressed: () {},
                child: const Text('Comenzar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
