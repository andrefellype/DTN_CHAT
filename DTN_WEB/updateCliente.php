<?php

include_once './conexao.php';

$json = NULL;
$lista = array();
header("Content-type:application/json");
$json = file_get_contents('php://input');
if ($json != NULL) {
    $json = json_decode($json);
    $id = $json->id_servidor;
    $nome = $json->nome;
    $telefone = $json->telefone;
    mysqli_query($con, "UPDATE cliente_rede SET nome = '" . $nome . "', telefone = '" . $telefone . "' WHERE id = '" . $id . "'");
    $query = mysqli_query($con, "SELECT * FROM cliente_rede WHERE id = '" . $id . "'");
    if ($l = mysqli_fetch_array($query)) {
        array_push($lista, $l);
    }
    echo json_encode($lista);
} else {
    echo "Nenhum dado foi informado";
}