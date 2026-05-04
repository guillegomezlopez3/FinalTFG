import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../../providers/workout_provider.dart';
import '../../../providers/user_provider.dart';
import '../../../utils/app_colors.dart';
import '../../../models/workout.dart';

class WorkoutsScreen extends StatefulWidget {
  const WorkoutsScreen({super.key});

  @override
  State<WorkoutsScreen> createState() => _WorkoutsScreenState();
}

class _WorkoutsScreenState extends State<WorkoutsScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final user = context.read<UserProvider>().userProfile;
      if (user != null && user.clientId != null) {
        context.read<WorkoutProvider>().fetchClientWorkoutPlans(user.clientId!);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.background,
      appBar: AppBar(
        title: const Text('Entrenamientos'),
      ),
      body: Consumer<WorkoutProvider>(
        builder: (context, workoutProvider, child) {
          if (workoutProvider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (workoutProvider.errorMessage != null && workoutProvider.workoutPlans.isEmpty) {
            return Center(
              child: Text(workoutProvider.errorMessage!),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: workoutProvider.workoutPlans.length,
            itemBuilder: (context, index) {
              final plan = workoutProvider.workoutPlans[index];
              return _WorkoutPlanCard(plan: plan);
            },
          );
        },
      ),
    );
  }
}

class _WorkoutPlanCard extends StatelessWidget {
  final WorkoutPlan plan;

  const _WorkoutPlanCard({required this.plan});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 20),
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Expanded(
                  child: Text(
                    plan.title,
                    style: const TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                      color: AppColors.primary,
                    ),
                  ),
                ),
                if (plan.active)
                  const Chip(
                    label: Text('Activo', style: TextStyle(fontSize: 10, color: Colors.white)),
                    backgroundColor: Colors.green,
                    padding: EdgeInsets.zero,
                  ),
              ],
            ),
            if (plan.objective != null)
              Padding(
                padding: const EdgeInsets.only(top: 8.0),
                child: Text(
                  'Objetivo: ${plan.objective}',
                  style: const TextStyle(fontStyle: FontStyle.italic),
                ),
              ),
            const Divider(height: 32),
            const Text(
              'Días de Entrenamiento:',
              style: TextStyle(fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 12),
            ...plan.workoutDays.map((day) => _WorkoutDayTile(day: day)),
          ],
        ),
      ),
    );
  }
}

class _WorkoutDayTile extends StatelessWidget {
  final WorkoutDay day;

  const _WorkoutDayTile({required this.day});

  @override
  Widget build(BuildContext context) {
    return ExpansionTile(
      title: Text(
        day.dayOfWeek,
        style: const TextStyle(fontWeight: FontWeight.w600),
      ),
      subtitle: Text(day.focus ?? 'General'),
      leading: const Icon(Icons.calendar_today, size: 20),
      children: day.exercises.map((ex) => _ExerciseItem(exercise: ex)).toList(),
    );
  }
}

class _ExerciseItem extends StatelessWidget {
  final Exercise exercise;

  const _ExerciseItem({required this.exercise});

  @override
  Widget build(BuildContext context) {
    return ListTile(
      dense: true,
      title: Text(exercise.name, style: const TextStyle(fontWeight: FontWeight.bold)),
      subtitle: Text(
        '${exercise.sets} series x ${exercise.reps} • ${exercise.restSeconds}s descanso',
      ),
      trailing: const Icon(Icons.info_outline, size: 18),
    );
  }
}
