import React, {Component} from 'react';
import {Button, StyleSheet, Text, View} from "react-native";
import Toast from "react-native-easy-toast";
import {AccessToken, LoginButton} from "react-native-fbsdk";

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
                    <LoginButton
                        publishPermissions={["public_profile", "email"]}
                        onLoginFinished={this.loginWithFacebook}/>
                </View>
                <View style={styles.button}>
                    <Button title="Github" onPress={this.loginWithGithub}/>
                </View>
                <Toast ref="toast"/>
            </View>
        );
    }

    loginByEmail() {
        this.refs.toast.show('Not implemented!');
    }

    loginByPhone() {
        this.refs.toast.show('Not implemented!');
    }

    loginWithGoogle() {
        this.refs.toast.show('Not implemented!');
    }

    loginWithFacebook(error, result) {
        if (error) {
            this.refs.toast.show(result.error);

        } else {
            AccessToken.getCurrentAccessToken().then(token => {
                const credential = new firebase.auth.FacebookAuthProvider.credential(token);

                firebase.auth().signInWithCredential(credential)
            })
        }
    }

    loginWithGithub() {
        this.refs.toast.show('Not implemented!');
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
