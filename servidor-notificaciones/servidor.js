const admin = require("firebase-admin");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

let primeraCarga = true;

console.log("Servidor local de notificaciones iniciado...");
console.log("Escuchando nuevas mascotas en Firestore...");

db.collection("mascotas")
    .onSnapshot(async (snapshot) => {
        if (primeraCarga) {
            primeraCarga = false;
            console.log("Mascotas existentes cargadas. Esperando nuevas publicaciones...");
            return;
        }

        for (const change of snapshot.docChanges()) {
            if (change.type === "added") {
                const mascota = change.doc.data();
                const mascotaId = change.doc.id;

                await enviarNotificacion(mascota, mascotaId);
            }
        }
    }, (error) => {
        console.error("Error escuchando mascotas:", error);
    });

async function enviarNotificacion(mascota, mascotaId) {
    const nombre = mascota.nombre || "Una mascota";
    const estado = mascota.estado || "";
    const tipo = mascota.tipo || "mascota";

    let titulo = "Nueva publicación en PetFect";
    let cuerpo = `${nombre} ha sido publicada en PetFect.`;

    if (estado === "perdido") {
        titulo = "Mascota perdida";
        cuerpo = `${nombre} se ha perdido. ¿Puedes ayudar?`;
    } else if (estado === "adopcion") {
        titulo = "Mascota en adopción";
        cuerpo = `${nombre} busca un nuevo hogar.`;
    }

    const message = {
        notification: {
            title: titulo,
            body: cuerpo,
        },
        data: {
            mascotaId: mascotaId,
            nombre: nombre,
            estado: estado,
            tipo: tipo,
        },
        topic: "allUsers",
    };

    try {
        const response = await admin.messaging().send(message);
        console.log("Notificación enviada:", nombre);
        console.log("Respuesta FCM:", response);
    } catch (error) {
        console.error("Error enviando notificación:", error);
    }
}