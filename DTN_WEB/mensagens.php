<?php

include_once './conexao.php';

$json = NULL;
$lista = array();
header("Content-type:application/json");
$json = file_get_contents('php://input');
if ($json != NULL) {
    $json = json_decode($json);
    $usuario = $json->id_servidor;
    $query = mysqli_query($con, "SELECT * FROM mensagem WHERE destinatario = '" . $usuario . "' AND status = FALSE");
    while ($l = mysqli_fetch_array($query)) {
        $l['status'] = 1;
        mysqli_query($con, "UPDATE mensagem SET status = TRUE WHERE id ='" . $l['id'] . "'");
        array_push($lista, $l);
    }
    echo json_encode($lista);
} else {
    echo "Nenhum dado foi informado";
}
