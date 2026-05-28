const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

admin.initializeApp();

exports.onMascotaCreated = onDocumentCreated(
  "mascotas/{mascotaId}",
  async (event) => {

    const mascota = event.data.data();

    const nombre = mascota.nombre || "Una mascota";
    const estado = mascota.estado || "";
    const tipo = mascota.tipo || "mascota";

    let titulo = "🐾 Nueva publicación en PetFect";
    let cuerpo = `${nombre} ha sido publicada`;

    if (estado === "perdido") {
      titulo = "🚨 Mascota perdida";
      cuerpo = `${nombre} se ha perdido`;
    } else if (estado === "adopcion") {
      titulo = "🏡 En adopción";
      cuerpo = `${nombre} busca hogar`;
    }

    const message = {
      notification: {
        title: titulo,
        body: cuerpo
      },
      data: {
        nombre,
        estado,
        tipo
      },
      topic: "allUsers"
    };

    await admin.messaging().send(message);

    console.log("✔ Notificación enviada:", nombre);
  }
);