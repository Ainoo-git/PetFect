const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

admin.initializeApp();

exports.onMascotaCreated = onDocumentCreated(
  "mascotas/{mascotaId}",
  async (event) => {

    const mascota = event.data.data();
    const mascotaId = event.params.mascotaId;

    const nombre = mascota.nombre || "Una mascota";
    const estado = mascota.estado || "";
    const tipo = mascota.tipo || "mascota";

    let titulo = "🐾 Nueva publicación en PetFect";
    let cuerpo = `${nombre} ha sido publicada en PetFect`;

    if (estado === "perdido") {
      titulo = "🚨 Mascota perdida 🚨";
      cuerpo = `${nombre} se ha perdido. ¿Puedes ayudar?`;
    } else if (estado === "adopcion") {
      titulo = "🏡 Mascota en adopción 🏡";
      cuerpo = `${nombre} busca un nuevo hogar`;
    }

    const message = {
      notification: {
        title: titulo,
        body: cuerpo
      },
      data: {
        mascotaId: mascotaId,
        nombre: nombre,
        estado: estado,
        tipo: tipo
      },
      topic: "allUsers"
    };

    await admin.messaging().send(message);

    console.log("Notificación enviada:", nombre);
  }
);