import { useNavigation } from "@react-navigation/native";
import React, { useState, useRef } from "react";
import {
  ScrollView,
  View,
  Text,
  Dimensions,
  StyleSheet,
  Image,
  TouchableOpacity,
} from "react-native";

const { width } = Dimensions.get("window");

const MyBanner = () => {
  const Navigation = useNavigation();
  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollViewRef = useRef();

  const handleScroll = (event) => {
    const contentOffsetX = event.nativeEvent.contentOffset.x;
    const newIndex = Math.round(contentOffsetX / width);
    setCurrentIndex(newIndex);
  };

  return (
    <View style={styles.carouselContainer}>
      <ScrollView
        horizontal={true}
        pagingEnabled={true}
        showsHorizontalScrollIndicator={false}
        style={styles.scrollViewStyle}
        onScroll={handleScroll}
        ref={scrollViewRef}
        scrollEventThrottle={16}
      >
        <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#209FF9",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between",
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                },
              ]}
            >
              <View
                style={[
                  {
                    alignItems: "center",
                    marginHorizontal: 10,
                  },
                ]}
              >
                <Text style={styles.text}>커피농장 투자</Text>
                <Text style={styles.text}>시작해볼까요?</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{ width: 150, height: 150, resizeMode: "contain" }}
              />
            </View>
            <View style={styles.indicatorContainer}>
              {[...Array(3).keys()].map((index) => (
                <View
                  key={index}
                  style={[
                    styles.indicator,
                    currentIndex === index && styles.activeIndicator,
                  ]}
                />
              ))}
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#C3A995",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between",
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                },
              ]}
            >
              <View
                style={[
                  {
                    alignItems: "center",
                    marginHorizontal: 10,
                  },
                ]}
              >
                <Text style={styles.text}>커피농장 투자</Text>
                <Text style={styles.text}>시작해볼까요?</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{ width: 150, height: 150, resizeMode: "contain" }}
              />
            </View>
            <View style={styles.indicatorContainer}>
              {[...Array(3).keys()].map((index) => (
                <View
                  key={index}
                  style={[
                    styles.indicator,
                    currentIndex === index && styles.activeIndicator,
                  ]}
                />
              ))}
            </View>
          </View>
        </TouchableOpacity>

        <TouchableOpacity onPress={() => Navigation.navigate("announcement")}>
          <View
            style={[
              styles.item,
              {
                backgroundColor: "#4E4E4E",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "space-between",
              },
            ]}
          >
            <View
              style={[
                {
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between",
                },
              ]}
            >
              <View
                style={[
                  {
                    alignItems: "center",
                    marginHorizontal: 10,
                  },
                ]}
              >
                <Text style={styles.text}>커피농장 투자</Text>
                <Text style={styles.text}>시작해볼까요?</Text>
              </View>
              <Image
                source={require("../../../assets/images/farmfarm.png")}
                style={{ width: 160, height: 160, resizeMode: "contain", marginLeft:30}}
              />
            </View>
            <View style={styles.indicatorContainer}>
              {[...Array(3).keys()].map((index) => (
                <View
                  key={index}
                  style={[
                    styles.indicator,
                    currentIndex === index && styles.activeIndicator,
                  ]}
                />
              ))}
            </View>
          </View>
        </TouchableOpacity>

      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  header: {
    fontFamily: "pretendard-extraBold",
    fontSize: 23,
    paddingBottom: 20,
  },
  carouselContainer: {
    // 새로운 스타일
    marginTop: 10,
    position: "relative",
  },
  scrollViewStyle: {
    width: "100%",
  },
  item: {
    width: width - 40,
    borderRadius: 25,
    marginTop: 10,
    marginHorizontal: 20,
    height: 160,
    justifyContent: "center",
    alignItems: "center",
  },
  text: {
    fontFamily: "pretendard-extraBold",
    fontSize: 28,
    color: "white",
  },
  indicatorContainer: {
    marginLeft:10,
    position: "absolute",
    bottom: 10,
    left: 20, // 왼쪽 아래 정렬을 위해 변경
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
  },
  indicator: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "lightgray",
    margin: 5,
  },
  activeIndicator: {
    backgroundColor: "white",
  },
});

export default MyBanner;
