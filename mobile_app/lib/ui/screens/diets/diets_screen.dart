import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/diet_provider.dart';
import '../../../providers/user_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/diet.dart';

/// Pantalla que muestra los planes nutricionales (dietas) del usuario.
///
/// Permite visualizar los macronutrientes totales y marcar comidas como completadas.
class DietsScreen extends StatefulWidget {
  const DietsScreen({super.key});

  @override
  State<DietsScreen> createState() => _DietsScreenState();
}

/// Estado de la pantalla de dietas que gestiona la carga de datos del cliente.
class _DietsScreenState extends State<DietsScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final user = context.read<UserProvider>().userProfile;
      if (user != null && user.clientId != null) {
        context.read<DietProvider>().fetchClientDiets(user.clientId!);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 120.0,
            floating: false,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              title: const Text(
                'Nutrición',
                style: TextStyle(
                  color: AppColors.text,
                  fontWeight: FontWeight.w900,
                ),
              ),
              centerTitle: true,
              background: Container(color: AppColors.background),
            ),
            backgroundColor: AppColors.background.withOpacity(0.8),
            elevation: 0,
          ),
          Consumer<DietProvider>(
            builder: (context, dietProvider, child) {
              if (dietProvider.isLoading) {
                return const SliverFillRemaining(
                  child: Center(child: CircularProgressIndicator()),
                );
              }

              if (dietProvider.errorMessage != null &&
                  dietProvider.diets.isEmpty) {
                return SliverFillRemaining(
                  child: Center(
                    child: Padding(
                      padding: const EdgeInsets.all(32.0),
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Icon(
                            Icons.restaurant_menu_rounded,
                            size: 64,
                            color: AppColors.textMuted.withOpacity(0.2),
                          ),
                          const SizedBox(height: 16),
                          Text(
                            dietProvider.errorMessage!,
                            textAlign: TextAlign.center,
                            style: const TextStyle(color: AppColors.textMuted),
                          ),
                        ],
                      ),
                    ),
                  ),
                );
              }

              return SliverPadding(
                padding: const EdgeInsets.all(16),
                sliver: SliverList(
                  delegate: SliverChildBuilderDelegate((context, index) {
                    final diet = dietProvider.diets[index];
                    return _DietCard(diet: diet);
                  }, childCount: dietProvider.diets.length),
                ),
              );
            },
          ),
        ],
      ),
    );
  }
}

/// Tarjeta que representa una dieta completa y su resumen de macros.
class _DietCard extends StatelessWidget {
  final Diet diet;

  const _DietCard({required this.diet});

  @override
  Widget build(BuildContext context) {
    // Calcular totales
    int totalCals = 0;
    double totalProt = 0;
    double totalCarbs = 0;
    double totalFats = 0;
    int completedCount = 0;

    for (var meal in diet.meals) {
      totalCals += meal.calories ?? 0;
      totalProt += meal.protein ?? 0;
      totalCarbs += meal.carbs ?? 0;
      totalFats += meal.fats ?? 0;
      if (meal.completed) completedCount++;
    }

    final double progressVal = diet.meals.isEmpty
        ? 0
        : completedCount / diet.meals.length;

    return Container(
      margin: const EdgeInsets.only(bottom: 24),
      decoration: BoxDecoration(
        color: Colors.white,
        borderRadius: BorderRadius.circular(32),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.03),
            blurRadius: 25,
            offset: const Offset(0, 12),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Container(
                      padding: const EdgeInsets.all(12),
                      decoration: BoxDecoration(
                        color: AppColors.primary.withOpacity(0.1),
                        borderRadius: BorderRadius.circular(16),
                      ),
                      child: const Icon(
                        Icons.auto_awesome_rounded,
                        color: AppColors.primary,
                        size: 24,
                      ),
                    ),
                    const SizedBox(width: 16),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            diet.title,
                            style: const TextStyle(
                              fontSize: 20,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                          Text(
                            diet.description ?? 'Plan nutricional activo',
                            style: TextStyle(
                              color: AppColors.textMuted.withOpacity(0.7),
                              fontSize: 13,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 24),
                // Macro Summary
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    _MacroItem(
                      label: 'CALORÍAS',
                      value: '$totalCals',
                      icon: Icons.local_fire_department_rounded,
                      color: Colors.orange,
                      bgColor: Colors.orange.withOpacity(0.05),
                    ),
                    _MacroItem(
                      label: 'PROT (G)',
                      value: totalProt.toStringAsFixed(0),
                      icon: Icons.egg_alt_rounded,
                      color: Colors.red,
                      bgColor: Colors.red.withOpacity(0.05),
                    ),
                    _MacroItem(
                      label: 'HC (G)',
                      value: totalCarbs.toStringAsFixed(0),
                      icon: Icons.bakery_dining_rounded,
                      color: Colors.blue,
                      bgColor: Colors.blue.withOpacity(0.05),
                    ),
                    _MacroItem(
                      label: 'GRASA (G)',
                      value: totalFats.toStringAsFixed(0),
                      icon: Icons.water_drop_rounded,
                      color: Colors.yellow[800]!,
                      bgColor: Colors.yellow[800]!.withOpacity(0.05),
                    ),
                  ],
                ),
                const SizedBox(height: 32),
                // Progress bar
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        const Text(
                          'PROGRESO DEL DÍA',
                          style: TextStyle(
                            fontWeight: FontWeight.w900,
                            fontSize: 10,
                            letterSpacing: 0.5,
                            color: AppColors.textMuted,
                          ),
                        ),
                        Text(
                          '${(progressVal * 100).toInt()}%',
                          style: const TextStyle(
                            fontWeight: FontWeight.w900,
                            color: AppColors.primary,
                            fontSize: 13,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 10),
                    Stack(
                      children: [
                        Container(
                          height: 10,
                          width: double.infinity,
                          decoration: BoxDecoration(
                            color: AppColors.primary.withOpacity(0.1),
                            borderRadius: BorderRadius.circular(5),
                          ),
                        ),
                        AnimatedContainer(
                          duration: const Duration(milliseconds: 500),
                          height: 10,
                          width:
                              (MediaQuery.of(context).size.width - 80) *
                              progressVal,
                          decoration: BoxDecoration(
                            gradient: const LinearGradient(
                              colors: [
                                AppColors.primary,
                                AppColors.primaryLight,
                              ],
                            ),
                            borderRadius: BorderRadius.circular(5),
                            boxShadow: [
                              BoxShadow(
                                color: AppColors.primary.withOpacity(0.3),
                                blurRadius: 6,
                                offset: const Offset(0, 2),
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ],
            ),
          ),
          const Divider(height: 1),
          ...diet.meals.map((meal) => _MealTile(meal: meal)),
          const SizedBox(height: 16),
        ],
      ),
    );
  }
}

/// Widget compacto para mostrar un valor de macronutriente.
class _MacroItem extends StatelessWidget {
  final String label;
  final String value;
  final IconData icon;
  final Color color;
  final Color bgColor;

  const _MacroItem({
    required this.label,
    required this.value,
    required this.icon,
    required this.color,
    required this.bgColor,
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 12),
      decoration: BoxDecoration(
        color: bgColor,
        borderRadius: BorderRadius.circular(16),
      ),
      child: Column(
        children: [
          Icon(icon, color: color, size: 18),
          const SizedBox(height: 6),
          Text(
            value,
            style: const TextStyle(fontWeight: FontWeight.w900, fontSize: 15),
          ),
          Text(
            label,
            style: TextStyle(
              color: AppColors.textMuted.withOpacity(0.6),
              fontSize: 8,
              fontWeight: FontWeight.w900,
              letterSpacing: 0.2,
            ),
          ),
        ],
      ),
    );
  }
}

/// Fila interactiva que representa una comida individual.
class _MealTile extends StatelessWidget {
  final DietMeal meal;

  const _MealTile({required this.meal});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 6),
      child: InkWell(
        onTap: () {
          context.read<DietProvider>().toggleMealCompletion(meal.id);
        },
        borderRadius: BorderRadius.circular(24),
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 300),
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: meal.completed
                ? Colors.green.withOpacity(0.06)
                : Colors.white,
            borderRadius: BorderRadius.circular(24),
            border: Border.all(
              color: meal.completed
                  ? Colors.green.withOpacity(0.2)
                  : Colors.black.withOpacity(0.03),
            ),
          ),
          child: Row(
            children: [
              Container(
                width: 52,
                height: 52,
                decoration: BoxDecoration(
                  color: meal.completed
                      ? Colors.green.withOpacity(0.1)
                      : AppColors.background.withOpacity(0.5),
                  borderRadius: BorderRadius.circular(18),
                ),
                child: Center(
                  child: Text(
                    _getEmoji(meal.mealType),
                    style: const TextStyle(fontSize: 26),
                  ),
                ),
              ),
              const SizedBox(width: 16),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Text(
                          meal.mealType.toUpperCase(),
                          style: TextStyle(
                            fontSize: 10,
                            fontWeight: FontWeight.w900,
                            letterSpacing: 1,
                            color: meal.completed
                                ? Colors.green
                                : AppColors.primary,
                          ),
                        ),
                        if (meal.mealTime != null) ...[
                          const SizedBox(width: 8),
                          Text(
                            '• ${meal.mealTime}',
                            style: TextStyle(
                              color: AppColors.textMuted.withOpacity(0.4),
                              fontSize: 10,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                        ],
                      ],
                    ),
                    const SizedBox(height: 4),
                    Text(
                      meal.formattedFoods,
                      style: TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.w800,
                        color: meal.completed
                            ? AppColors.textMuted
                            : AppColors.text,
                        decoration: meal.completed
                            ? TextDecoration.lineThrough
                            : null,
                        letterSpacing: -0.2,
                      ),
                    ),
                    if (meal.calories != null)
                      Padding(
                        padding: const EdgeInsets.only(top: 2.0),
                        child: Text(
                          '${meal.calories} KCAL',
                          style: TextStyle(
                            color: AppColors.textMuted.withOpacity(0.5),
                            fontSize: 11,
                            fontWeight: FontWeight.w900,
                          ),
                        ),
                      ),
                  ],
                ),
              ),
              AnimatedContainer(
                duration: const Duration(milliseconds: 300),
                padding: const EdgeInsets.all(6),
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: meal.completed
                      ? Colors.green
                      : Colors.grey.withOpacity(0.08),
                  border: Border.all(
                    color: meal.completed
                        ? Colors.transparent
                        : Colors.black.withOpacity(0.05),
                  ),
                ),
                child: Icon(
                  meal.completed ? Icons.check_rounded : Icons.add_rounded,
                  color: meal.completed ? Colors.white : Colors.grey[400],
                  size: 18,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _getEmoji(String type) {
    type = type.toLowerCase();
    if (type.contains('desayuno')) return '🍳';
    if (type.contains('almuerzo')) return '🥗';
    if (type.contains('cena')) return '🍲';
    if (type.contains('snack') || type.contains('merienda')) return '🍏';
    return '🍽️';
  }
}
