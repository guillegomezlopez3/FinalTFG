import 'dart:convert';
import 'api_client.dart';
import '../models/user_profile.dart';

class UserService {
  static const String meEndpoint = '/me';

  static Future<UserProfileResponse?> getMyProfile() async {
    try {
      final response = await ApiClient.get(meEndpoint);

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        return UserProfileResponse.fromJson(data);
      }
      return null;
    } catch (e) {
      return null;
    }
  }
}

