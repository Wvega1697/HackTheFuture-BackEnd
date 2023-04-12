# Williams Adolfo Vega Montenegro - williamsv1697@gmail.com

[![Landscape-of-Eco-Industrys.jpg](https://i.postimg.cc/MHmfcLzf/Leonardo-Creative-Landscape-of-Eco-Industrys-Sustainable-Diffus-0.jpg)](https://postimg.cc/PvN592pd)
> Imagen generada por IA

# EcoIndustry - HackTheFuture-BackEnd

EcoIndustry es una empresa comprometida con la reducción de la huella de carbono en su proceso de producción. Con el objetivo de lograr este objetivo, la empresa ha implementado técnicas innovadoras en sus instalaciones industriales. Como parte de su estrategia, EcoIndustry necesita recopilar información detallada de todas las áreas de la fábrica desde enero 2022 hasta diciembre 2022. Este proceso de recopilación de datos es esencial para la identificación de áreas críticas en las que se pueda reducir el consumo de energía y disminuir la emisión de gases contaminantes. En EcoIndustry, estamos comprometidos con el cuidado del medio ambiente y trabajamos constantemente para lograr un proceso de producción más sostenible y respetuoso con nuestro planeta.

## Infraestructura del proyecto

 Se utilizo una base de datos no relacional, creada en firebase.
 
 Para el codigo se utilizo Java 11 con Spring Boot y Maven.
 
 El tipo de estructura del proyecto es de Paquetes por capas.

## Equivalencia en los métodos solicitados para la API

| Método | Descripción | Equivalente dentro de la API |
| ------ | ----------- | ---------------------------- |
| POST | Registrar consumo de combustible | consume/add |
| POST | Registrar consumo de energía eléctrica | consume/add |
| POST | Registrar consumo de otros productos derivados del pertóleo | consume/add |
| POST | Registrar número de viajes | consume/add |
| PUT | Actualizar cantidad de consumo de combustible según su categoría | consume/edit/{type}/{categoryType}/{month}/{quantity} |
| GET | Calcular el porcentaje de consumo anual de combustible por categoría | consume/annualFuel |
| GET | Calcular el promedio mensual de consumo de combustible | consume/monthlyFuel |
| GET | Calcular qué segmento es el que más impacta | consume/segment |
| GET | Calcular el promedio mensual de uso de energía eléctrica únicamente en la planta de envasado | consume/packagingPlant |
| GET | Comparativa de uso mensual entre energía eléctrica y combustible | consume/monthlyConsumption |
| GET | Calcular el promedio de uso mensual de productos derivados del petróleo | consume/petroleumAverage |
| GET | Comparativa de promedio mensual de viajes equipo de ventas y administrativo | consume/monthlyTravels |
| GET | Mostrar los datos de uso de aceites mensualmente | consume/monthlyOil |
| GET | Devolver mes donde hubieron menos pérdidas de refrigerantes | consume/coolantMonth |
| GET | Obtener el mes que representa el uso menos vs el mayor cantidad de galones utilizados en combustible | consume/getGallons |


## Explicación de la estructura de los datos dentro de la API

| Enum | Tipo | Descripción |
| ------ | ------ | ----------- |
| Type | Tipo | Se utiliza para definir el tipo principal del consumo. |
| SubType | SubTipo | Se utiliza para definir el subtipo del consumo. |
| EmissionType | Emisión | Se utiliza para segmentar por tipo de emisión. |
| CategoryType | Categoría | Se utiliza para definir la categoria del consumo. |
| UnitOfMeasure | Unidad de medida | Se utiliza para definir la unidad de medida del consumo. |
| Month | Mes | Se utiliza para definir el mes del consumo. |

## Posibles valores para los tipos de datos dentro de la API

### Type - Tipo

| Valor | Descripción |
| ------ | ----------- |
| Fuel | Consumos de tipo: Combustible. |
| Electricity | Consumos de tipo: Energía eléctrica. |
| Petroleum-derived product | Consumos de Productos derivados del petróleo. |
| Travel | Viajes realizados en el mes. |
| Supply | Consumos de tipo: Suministros |

### SubType - SubTipo

| Valor | Descripción |
| ------ | ----------- |
| Fuel | Consumos de subtipo: Combustible. |
| Oil | Consumos de subtipo: Aceite. |
| Coolant | Consumos de subtipo: Refrigerante. |
| Sheets | Consumos de subtipo: Hojas de papel. |

### EmissionType - Emisión

| Valor | Descripción |
| ------ | ----------- |
| Direct | Emisión directa: asociada a las actividades de la organización. |
| Indirect | Emisión indirecta: asociada al consumo energético adquirido y consumido por la organización. |
| Other | Otras emisiones indirectas: asociadas a otras actividades no controladas por la  organización. |

### CategoryType - Categoría

| Valor | Descripción |
| ------ | ----------- |
| Admin | Consumos de la categoría: Administrativo. |
| Indirect | Consumos de la categoría: Indirecto de proveedor. |
| Logistic | Consumos de la categoría: Logística o logístico. |
| Operation | Consumos de la categoría: Operación. |
| Distribution | Consumos de la categoría: Distribución. |
| Sales | Consumos de la categoría: Ventas. |

### UnitOfMeasure - Unidad de medida

| Valor | Descripción |
| ------ | ----------- |
| Gallon | Galones de combustible consumidos. |
| Sheets | Hojas de papel utilizadas. |
| Kw | Kw de energía eléctrica consumidos. |
| Travel | Viajes realizados. |

### Month - Mes

| Valor | Descripción |
| ------ | ----------- |
| January | Valor para registrar el consumo del mes de Enero. |
| February | Valor para registrar el consumo del mes de Febrero. |
| March | Valor para registrar el consumo del mes de Marzo. |
| April | Valor para registrar el consumo del mes de Abril. |
| May | Valor para registrar el consumo del mes de Mayo. |
| June | Valor para registrar el consumo del mes de Junio. |
| July | Valor para registrar el consumo del mes de Julio. |
| August | Valor para registrar el consumo del mes de Agosto. |
| September | Valor para registrar el consumo del mes de Septiembre. |
| October | Valor para registrar el consumo del mes de Octubre. |
| November | Valor para registrar el consumo del mes de Noviembre. |
| December | Valor para registrar el consumo del mes de Diciembre. |

## Objeto de tipo "Consumo"

Los objetos de tipo "Consumo" (*ConsumeObject* dentro del API) se utilizan para registrar y generar los informes dentro de la API. Estos objetos se almacenan en la base de datos no relacional de firebase.

### Estructura de los objetos de tipo "Consumo" o "ConsumeObject"

| Valor | Descripción | Tipo de dato |
| ------ | ----------- | ----------- |
| internalId* | ID para referenciar al consumo. Se crea concatenando el NUM-MES_TIPO_CATEGORIA. | String |
| description | Permite agregar una descripción al consumo. | String |
| type | Define el tipo de consumo. | Type |
| emissionType | Define el segmento de emisión del consumo. | EmissionType |
| categoryType | Define la categoría del consumo. | CategoryType |
| unitOfMeasure | Define la unidad de medida del consumo. | UnitOfMeasure |
| quantity | Establece la cantidad que se consumio en el mes. | int |
| month | Define el mes del consumo. | Month |
| subType | Define el subtipo del consumo. | SubType |

*Adicional al internalId, firebase crea un ID unico al agregar el documento a la colección.

## Colección de Postman

A continuación encontrarás la colección de postman para exportarla, tiene ejemplos de cada método de la API.

[Consumption.postman_collection.zip](https://github.com/Wvega1697/HackTheFuture-BackEnd/files/11214225/Consumption.postman_collection.zip)
