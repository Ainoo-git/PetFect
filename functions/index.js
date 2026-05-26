const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

admin.initializeApp();

//Trigger moderno (Firebase v2)
exports.onPetCreated = onDocumentCreated("pets/{petId}", async (event) => {

    const pet = event.data.data();

    console.log("Nueva mascota:", pet);

    await admin.messaging().send({
        notification: {
            title: "Nueva mascota en PetFect",
            body: pet.name
                ? `${pet.name} ha sido publicada`
                : "Nueva mascota publicada"
        },
        topic: "allUsers"
    });

});