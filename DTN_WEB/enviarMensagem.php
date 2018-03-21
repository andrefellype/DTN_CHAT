<?php

include_once './conexao.php';

$json = null;
$lista = array();
header('Content-type:application/json');
$json = file_get_contents('php://input');
if ($json != NULL) {
    $json = json_decode($json);
    $emissor = $json->emissor;
    $destinatario = $json->destinatario;
    $texto = $json->texto;
    $status = $json->status ? 1 : 0;
    $data_envio = date("Y-m-d H:i:s", strtotime($json->data_envio));
    mysqli_query($con, "INSERT INTO mensagem(emissor, destinatario, texto, status, data_envio) VALUES('" . $emissor . "','" . $destinatario . "','" . $texto . "','" . $status . "','" . $data_envio . "')") or die (mysqli_error($con));
    $query = mysqli_query($con, "SELECT * FROM mensagem WHERE emissor = '" . $emissor . "' AND destinatario = '" . $destinatario . "' AND data_envio = '" . $data_envio . "'");
    if ($l = mysqli_fetch_array($query)) {
        array_push($lista, $l);
    }
    echo json_encode($lista);
} else {
    echo "Nenhum dado foi informado";
}
