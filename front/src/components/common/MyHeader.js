import { View, Text, StyleSheet, Image, Pressable } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation } from "@react-navigation/native";

import Colors from "../../constants/Colors";
import { useDispatch } from "react-redux";
import { setSearchModal } from "../../stores/fundingSlice";

const MyHeader = ({ children, isSearch }) => {
  const Navigation = useNavigation();
  const dispatch = useDispatch();

  const onPress = () => {
    dispatch(setSearchModal(true));
  };

  return (
    <View style={styles.container}>
      <View style={styles.imageContainer}>
        <Image
          style={styles.appLogo}
          source={require("../../../assets/images/RottoLogo.png")}
        />
        {
          isSearch ?
          <Pressable style={styles.searchIcon} onPress={onPress}>
            <Ionicons name="search" size={24} color="white" />
          </Pressable>
          :
          <View style={styles.iconContainer}>
            <Ionicons
              name="notifications-outline"
              size={26}
              color="white"
              onPress={() => Navigation.navigate("alertList")}
            />
            <Ionicons
              name="person-outline"
              size={26}
              color="white"
              onPress={() => Navigation.navigate("My")}
            />
          </View>
        }
      </View>
      {children}
    </View>
  );
};

export default MyHeader;

const styles = StyleSheet.create({
  appLogo: {
    marginLeft: 10,
  },
  container: {
    flex: 1,
    backgroundColor: Colors.bgOrg,
  },
  imageContainer: {
    flexDirection: "row",
    justifyContent: "space-between",
  },
  iconContainer: {
    width: 60,
    marginRight: 20,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
  },
  text: {
    color: "white",
  },
  searchIcon: {
    marginRight: 20,
    alignItems: 'center',
    justifyContent: 'center'
  }
});
