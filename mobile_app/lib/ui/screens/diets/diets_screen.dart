import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/diet_provider.dart';
import '../../../providers/user_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/diet.dart';

class DietsScreen extends StatefulWidget {
  const DietsScreen({super.key});

  @override
  State<DietsScreen> createState() => _DietsScreenState();
}

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
      appBar: AppBar(
        title: const Text('Mis Dietas'),
      ),
      body: Consumer<DietProvider>(
        builder: (context, dietProvider, child) {
          if (dietProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (dietProvider.errorMessage != null && dietProvider.diets.isEmpty) {
            return Center(
              child: Text(dietProvider.errorMessage!),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: dietProvider.diets.length,
            itemBuilder: (context, index) {
              final diet = dietProvider.diets[index];
              return _DietCard(diet: diet);
            },
          );
        },
      ),
    );
  }
}

class _DietCard extends StatelessWidget {
  final Diet diet;

  const _DietCard({required this.diet});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      child: ExpansionTile(
        title: Text(
          diet.title,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Text(
          diet.description ?? 'Sin descripción',
          maxLines: 1,
          overflow: TextOverflow.ellipsis,
        ),
        children: diet.meals.map((meal) => _MealItem(meal: meal)).toList(),
      ),
    );
  }
}

class _MealItem extends StatelessWidget {
  final DietMeal meal;

  const _MealItem({required this.meal});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                meal.mealType,
                style: const TextStyle(
                  fontWeight: FontWeight.w600,
                  color: AppColors.primary,
                ),
              ),
              if (meal.mealTime != null)
                Text(
                  meal.mealTime!,
                  style: const TextStyle(color: AppColors.textMuted, fontSize: 12),
                ),
            ],
          ),
          const SizedBox(height: 4),
          Text(meal.foods),
          if (meal.calories != null)
            Text(
              '${meal.calories} kcal',
              style: const TextStyle(fontSize: 12, fontStyle: FontStyle.italic),
            ),
          const Divider(),
        ],
      ),
    );
  }
}
