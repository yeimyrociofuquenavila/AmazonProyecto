# language: es
  # Escenario para verificar la búsqueda de un producto en Amazon

Característica: Búsqueda y carga de producto al carrito

  Antecedentes:
    Dado que el usuario está en la página de Amazon

  Esquema del escenario: Agregar al carrito un producto desde la página 2
    Cuando busca el artículo "<producto>"
    Y navega a la segunda página de resultados
    Y selecciona el tercer producto disponible
    Entonces agrega al carrito más de dos unidades si está disponible

    Ejemplos:
      | producto   |
      | Computador |
      | Alexa      |
      | Samsung    |
      | Televisor  |