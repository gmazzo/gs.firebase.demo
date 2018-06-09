import React, {Component} from 'react';
import {Button, StyleSheet, Text, View} from "react-native";

type Props = {};
export default class LoginScreen extends Component<Props> {

    constructor(props) {
        super(props);

        this.state = {
            user: null
        }
    }

    render() {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Login</Text>
                <View style={styles.button}>
                    <Button title="Email" onPress={this.loginByEmail}/>
                </View>
                <View style={styles.button}>
                    <Button title="Phone" onPress={this.loginByPhone}/>
                </View>
                <View style={styles.button}>
                    <Button title="Google" onPress={this.loginWithGoogle}/>
                </View>
                <View style={styles.button}>
                    <Button title="Facebook" onPress={this.loginWithFacebook}/>
                </View>
                <View style={styles.button}>
                    <Button title="Github" onPress={this.loginWithGithub}/>
                </View>
            </View>
        );
    }

    loginByEmail() {
    }

    loginByPhone() {
    }

    loginWithGoogle() {
    }

    loginWithFacebook() {
    }

    loginWithGithub() {
    }

}

const styles = StyleSheet.create({
    container: {
        margin: 10,
        alignItems: 'center',
    },
    title: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 10,
    },
    button: {
        margin: 10,
        width: 200,
    },
});
